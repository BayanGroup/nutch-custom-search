Custom Search Tools For Nutch
=============================

The goal of this project is to develop plugins/extensions for Nutch to make it a perfect tool for building custom search solutions.
The project is an open-source project released under Apache License Version 2.0.

Note: This project is tested with nutch 1.7-1.9 (nutch 2.X is not supported yet).

Table of Contents
-----------------
* [Extractor](#extractor)
  * [Setup](#setup)
  * [Configuration](#configuration)
    * [Functions](#functions)
    * [Types](#types)
    * [Fields](#fields)
    * [Documents](#documents)
  * [Usage](#usage)
    * [Parsing HTML documents](#parsing-html-documents)
    * [Parsing XML documents](#parsing-xml-documents)
    * [Using without nutch (standalone)](#using-without-nutch-standalone)
  * [Troubleshooting](#troubleshooting)

Extractor
---------

Extractor helps to extract specific parts of an html/xml page using xpath expressions or css selectors.
The extracted parts are copied into one or several fields.


Extractor consists of a parser plugin, an html parser filter, and an indexer filter. You can use extractor in two modes:

1. Use extractor to extract additional fields in addition of the standard html parser fields (such as title, metatags, outlinks,...). In this mode, extractor is attached to the standard html parser as a parser filter. Html parser first parses the content, extracts its fields and outlinks, and then passes the content to the extractor parser filter. Extractor, parses the content again and adds some additional fields to the parse metadata. Later, the extractor indexer filter reads this parse metadata and adds the corresponding fields to index documents.

2. Use extractor as a standalone parser. In this mode, extractor is responsible for the whole parsing process including the extraction of title, outlinks, .... In this mode, you can use extractor to parse both xml and html files. 

### Setup

1) Download zal.extractor-X-distribution.zip from the [realses page](https://github.com/BayanGroup/nutch-custom-search/releases) and extract it to your nutch's plugins directory. After the extraction, you must have a directory named "extractor" under your nutch's plugins directory. In "extractor" directory, there must be several jars with a file named "plugin.xml".  Note that the jar is built with java 6.

2) Enable extractor plugin by adding its name (which is 'extractor') to the plugin.includes property in the nutch-site.xml (inside nutch conf directory). 

If you are using mode 2, you can disable other parser/indexer plugins, if you don't need them. For instance, you can use something like this:

```xml
<property>
  <name>plugin.includes</name>
  <value>protocol-http|urlfilter-regex|extractor|scoring-opic|urlnormalizer-(pass|regex|basic)</value>
</property>
```

3) If you are using mode 2, you should introduce the extractor parser to nutch by modifying the parse-plugins.xml (inside nutch conf directory). First, add its alias in the aliases section:

```xml
<aliases>
	...
	<alias name="extractor" extension-id="ir.co.bayan.simorq.zal.extractor.nutch.ExtractorParser" />
</aliases>
```

Next, if you want the extractor to parse all content types, add it to the mimeType with name="*":

```xml
<mimeType name="*">
	<plugin id="extractor" />
	<plugin id="parse-tika" />
</mimeType>
```

Or if you want to use it for specific content types, add its name to the corresponding mimeTypes:

```xml
<mimeType name="text/html">
	<plugin id="extractor" />
	<plugin id="parse-html" />
</mimeType>

<mimeType name="application/rss+xml">
	<plugin id="extractor" />
	<plugin id="parse-tika" />
	<plugin id="feed" />
</mimeType>
```

4) Add your extractors.xml to the nutch conf directory. A simple extractors.xml is look like this:

```xml
<config xmlns="http://bayan.ir" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://bayan.ir http://raw.github.com/BayanGroup/nutch-custom-search/master/zal.extractor/src/main/resources/extractors.xsd">
	<fields>
		<field name="title" />
	</fields>
	<documents>
		<document url="^http://.+?\.google\.com" engine="css">
			<extract-to field="title">
				<text>
					<expr value="head > title" />
				</text>
			</extract-to>
		</document>
	</documents>
</config>
```

You can define a different name for this file by defining extractor.file property in nutch-site.xml:

```xml
<property>
  <name>extractor.file</name>
  <value>custom-extractors.xml</value>
</property>
```

### Configuration

The main configuration file is extractors.xml. This file has three sections:

1. types: you can define your required types and their corresponding converters here. This section is optional.
2. fields: contains all the fields that the extracted parts should be put into them. If indexing target is solr, these fields should be define in the solrschema.xml file too to be recognizable by solr. Each field has a name and an optional type which is one of the types defined in the types section. If no type is specified, the field is considered as of type string.
3. documents: contains one or several documents.

Each document consists of several matching criteria and several extract-to rules.
In the single match mode, when a resource (such as a web page) is passed to extractor, it looks for the first document that matches with this resource.
It then applies the extract-to rules of this document to this resource. In the multiple match mode, the extract-to rules of all the matching documents are applied.
An extract-to rule extracts a value from the resource and put the extracted value into its defined field which is one of the fields defined in the fields section. The value of the field is extracted by means of functions.
The matching criteria of a document are url , content type, and ip address. All the criteria are expressed by means of regex expressions.

The root element of extractors.xml must be named "config" and define the namespace "http://bayan.ir" as the default namespace. This element has the following optional attributes:

* omitNonMatching: if true and the resource url dose not match with any of the urls defined by documents, then that resource will not be indexed. Default value is false.
* filterNonMatching: if true, all urls that don't match with any of the urls defined by the documents will be filtered (not crawled or parsed or indexed). Default value is false.
* defaultEngine: the default engine for parsing contents of resources, if it is not defined by document (or any of its ancestors). Default value is css.
* matchMode: determines when multiple documents are matched, how to select them to apply extract-to rules. Default value is single.


Here is a sample extractors.xml file containing all of the above sections:

```xml
<config xmlns="http://bayan.ir" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://bayan.ir http://raw.github.com/BayanGroup/nutch-custom-search/master/zal.extractor/src/main/resources/extractors.xsd">
	<types>
		<type name="long" converter="ir.co.bayan.simorq.zal.extractor.convert.LongConverter" />
	</types>
	<fields>
		<field name="num-items" type="long" />
		<field name="all-items" />
	</fields>
	<documents>
		<document url="^http://.+?\.google\.com/?$" engine="css">
			<extract-to field="num-items">
				<size>
					<expr value="li .gbt" />
				</size>
			</extract-to>
			<extract-to field="all-items">
				<truncate max="100">
					<concat delimiter=",">
						<text>
							<expr value="li.gbt span.gbts" />
						</text>
					</concat>
				</truncate>
			</extract-to>
		</document>
	</documents>
</config>
```

The purpose of this file is to extract the number of items in the topmost bar of the google homepage and put it in a field named num-items. We also extract the concatenation of their names into the field named all-items. 
In this file, we have one type named long with a converter. This converter is used to convert the extracted value (a string) to the desired type.

In the fields section, we have a field named "num-items" of type long and a field named "all-items" of type string.
In the documents section, we defined a document which accepts all resources with url ending with .google.com. Since the extractor uses the partial regex matching we could write this as "google\.com/?$" too. The document specifies that its content should be parsed using the css engine.

This document has two extract-to rules. The first rule consists of two nested functions size and expr. 
The "expr" function returns a set of objects by querying the content using the provided engine. Here since our engine is css, li.gbt means all li elements with class .gbt.
The size function, returns the number of items in its argument which here is the list of elements that satisfy li.gbt expression.
This extracted value is copied into a field named "num-items".
In the second rule, we first extract the text value of all li.gbt span.gbts nodes, then concat them with comma as the separator and then limit their size to have less than 100 charcters.

#### Functions

Functions are used inside the extract-to rules to compute a value from the parsed content. The following table lists the available functions which can be used in the extract-to rules. You can find their descriptions and attributes in the schema file.

All functions output a list of objects and take as input a list of objects. Hence they can be nested (chained) to use output of one function as an input of another function. Note that when Extractor wants to compute the value of an extract-to rule, it first calls the chain of functions and then the returned list first concatenated (with space as the separator char) and then, after a possible conversion, if this value is not null or empty, it will be copied to the field.

Function name | Description
------------- | -----------
attribute | Extracts the value of an attribute with the specified name from the input elements.
concat | Concats its inputs by the provided delimiter.
constant | Always returns a fixed constant.
decode | Decodes the given url string.
default | Returns its first non-empty argument. An argument is empty if it is null, 0-sized, or contains only empty strings.
expr | Evaluates an expression using the current engine and returns the list of resulted elements. The evaluation is done in the scope of current root. By default the document elemen is the root unless it is changed using for-each, root attribute of document element, or a fragment. By default expression "." refers to the current root.
fetch | Fetches a content from the given url and evaluates it with the specified engine. (Experimental)
field-value | Returns the extracted value of the given field.
first | Returns the first object in the list of its argument.
for-each |  Iterates through its children with the given root as the new root.
last | Returns the last object in the list of its argument.
link  | Returns a set of links with href and anchors.
replace | Replaces its input using the provided regex pattern by the provided substitution.
resolve | Resolves a possible relative url to absolute one based on the current url in the context.
size | Returns the number of objects in its argument (which is a list).
text  | Returns the text content of its input elements.
truncate | Truncates a string if its size is greater than max.
trim | Trims a string.
url | Returns the current url (the url of matched resource) in the context.

#### Types
Each field might have a type. The following types are available out of the box. You can define your own types and converters by implementing the ir.co.bayan.simorq.zal.extractor.convert.Converter interface.
Extractor only creates one converter instance for each type.

```xml
<types>
	<type name="string" />
	<type name="long" converter="ir.co.bayan.simorq.zal.extractor.convert.LongConverter" />
	<type name="float" converter="ir.co.bayan.simorq.zal.extractor.convert.FloatConverter" />
	<type name="date" converter="ir.co.bayan.simorq.zal.extractor.convert.DateConverter" />
	<type name="date-time" converter="ir.co.bayan.simorq.zal.extractor.convert.DateTimeConverter" />
</types>
```

Type | Description
------------- | -----------
string | (the default type of nothing is specified)
long | converts string to long
float | converts string to float
date-time | converts a string in the yyyy-MM-dd'T'HH:mm:ss format to a date
date | converts a string in the dd/MM/yyyy format to a date

Note that the type conversion is only applied during the indexing phase.

#### Fields

Each field has these attributes:

* name: name of the field (required)
* type: the type of this field. must be one of the types defined in the types section. default value: string.
* multi: if true, field can get multiple values from extract-to rules. default value: false
* index: if true, the field will be added during indexing. default value: true. Note that if indexing target is solr, this fields should be define in the solrschema.xml file too to be recognizable by solr
* indexAs: specifies a different name as the name of the field for indexing. default value: the name of field.

There are three implicit fields that you might use them without needing to be defined:

* url: the url of the current document. This is mandatory and defaults to the matched resource url.
* title: the title of the current document. In mode 2, if title is not specified, the url will be used as the title.
* content: the textual content of this document which can be used by other nutch plugins (for instance the TextProfileSignature use this).

For each field, you can set its "multi" attribute to true which enables multiple values to be defined for this field. In this case, the value of this field is a list and each extracted item are added to this list. Note that you must also change your solr schema for that field to accept multiple values. As an example, suppose in our google example mentioned above, we wanted  "all-items" field to be a multi-value field. We can write our extractors.xml like this:

```xml
<config xmlns="http://bayan.ir" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://bayan.ir http://raw.github.com/BayanGroup/nutch-custom-search/master/zal.extractor/src/main/resources/extractors.xsd">
	<fields>
		<field name="all-items" multi="true"/>
	</fields>
	<documents>
		<document url="^http://.+?\.google\.com/?$" engine="css">
			<extract-to field="all-items">
				<text>
					<expr value="li.gbt span.gbts" />
				</text>
			</extract-to>
		</document>
	</documents>
</config>
```

Note that if a field is multi field, you can write multiple extract-to rules for it and all these values are added to it. For instance, in the following example, we always add a fixed item named "tools" besides the available google top-bar items:

```xml
<config xmlns="http://bayan.ir" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://bayan.ir http://raw.github.com/BayanGroup/nutch-custom-search/master/zal.extractor/src/main/resources/extractors.xsd">
	<fields>
		<field name="all-items" multi="true"/>
	</fields>
	<documents>
		<document url="^http://.+?\.google\.com/?$" engine="css">
			<extract-to field="all-items">
				<text>
					<expr value="li.gbt span.gbts" />
				</text>
			</extract-to>
			<extract-to field="all-items">
				<constant value="tools" />
			</extract-to>
		</document>
	</documents>
</config>
```

#### Documents

Each document can define three regex patterns as its matching criteria:
* url: specifies with what urls this document can be matched.
* contentType: specifies the content types of resources that this document can be matched to.
* ip: specifies the ip address of serving host of resource that must be matched.
These three criteria are ANDed together to determine whether the resource is matched or not.


Each document can specify one engine for parsing its content. Currently there are three engines available:
* css: parses the content using Jsoup library and is able to answer css selectors expressions
* xpath: uses the standard JAXP infrastructure and is able to answer xpath expressions
* txt: suitable for line oriented processing of text files. 


Each document may specify an outlinks section that tells extractor how outlinks should be extracted from the current content. Here are two samples:

```xml
<document url=".">
	<outlinks>
		<link>
			<href>
				<concat delimiter="">
					<url />
					<constant value="sitemap.xml" />
				</concat>
			</href>
		</link>
	</outlinks>
</document>
```

```xml
<document url="." engine="css">
	<outlinks>
		<link>
			<href>
				<resolve>
					<attribute name="href">
						<expr value="a[rel!=nofollow], link[rel=canonical]"/>
					</attribute>
				</resolve>
			</href>
		</link>
	</outlinks>
</document>
```

Also each document can have an id and other documents can inherit its fields and outlinks by specifying its id in their inherits attribute. For example:

```xml
<document id="base" engine="css">
	<extract-to field="display-url">
		<decode>
			<url />
		</decode>
	</extract-to>
	<extract-to field="site">
		<replace pattern="^.+?://(.+?)/.*" substitution="$1">
			<url />
		</replace>
	</extract-to>
</document>
<document id="common" url="." inherits="base">
	<extract-to field="modification-date" >
		<attribute name="content">
			<expr value="meta[name=date]" />
		</attribute>
	</extract-to>	
</document>
```

The inheritance chain can be of any length but note that the evaluation engine can not be changed along the inheritance hierarchy.

There is an option to extract several documents from one given resource. To enable this, you can specify a root for your document or define one or several fragments inside your document each with a root. The root must be an expression in terms of the current engine, that specifies the new root for evaluation. Since each fragment is going to be a distinct document recognizable by nutch, it is required to have a field named "url" inside your fragment. You can also use the predifined fields such as title and content insider your fragment, to specify the title and content of your extracted document. As an example:

```xml
<document url="test.html" engine="css">
	<fragment root=".doc">
		<extract-to field="url">
			<first>
				<attribute name="href">
				 	<expr value="a"/>
				 </attribute>
			</first>
		</extract-to>
		<extract-to field="content">
			<text>
				<expr value="a" />
			</text>
		</extract-to>
	</fragment>
</document>
```

In the above example, for each html element with css class "doc", one document will be created. The url of this document is extracted from the first href inside this element.

### Usage

#### Parsing HTML documents
(TODO)

#### Parsing XML documents
You can use extractor plugin to extract parts of xml documents. First, you must configure the plugin in mode 2 and set it as the default parser for xml mime types. Second, you must set your evaluation engine to xpath in your documents since clearly css engine can not parse xml documents. As an example, suppose we want to extract all person names from the following xml file:

```xml
<persons>
	<person>
		<name>Foo</name>
		<addr>Somewhere</addr>
	</person>
	<person>
		<name>Bar</name>
		<addr>SomewhereElse</addr>
	</person>
</persons>
```

We can use a configuration like this:

```xml
<config xmlns="http://bayan.ir" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://bayan.ir http://raw.github.com/BayanGroup/nutch-custom-search/master/zal.extractor/src/main/resources/extractors.xsd">
	<fields>
		<field name="content"/>
	</fields>
	<documents>
		<document url="*.xml" engine="xpath">
			<extract-to field="content">
				<concat delimiter=",">
					<text>
						<expr value="//person/name" />
					</text>
				</concat>
			</extract-to>
		</document>
	</documents>
</config>
```

If your xml elements defined in a specific namespace, you should write your xpath with ns_name:elem_name syntax. If your xml elements are defined in the default namespace you can refer to this namespace by "dns" as the namespace name.  For example, if your xml is look like this:

```xml
<persons xmlns="http://bayan.ir" xmlns:b="http://bayan.org" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://bayan.ir data.xsd">>
	<person>
		<b:name>Foo</b:name>
		<b:addr>Somewhere</b:addr>
	</person>
	<person>
		<b:name>Bar</b:name>
		<b:addr>SomewhereElse</b:addr>
	</person>
</persons>
```

Then your xpath expression should look like this: (Note that in the above xml file, the "person" element defined in the default namespace but the "name" element is defiend in the "b" namespace.)

```xml
	<expr value="//dns:person/b:name" />
```

You can extract links for further crawling from xml documents too. Here is an example that extract links from a sitemap file like this:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<?xml-stylesheet type="text/xsl" href="/media/css/sitemap/sitemap.xsl?ataLCF"?>
<urlset xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xmlns="http://www.sitemaps.org/schemas/sitemap/0.9"
		xmlns:blog="http://schemas.bayan.ir/2012/blog"
		xsi:schemaLocation="http://www.sitemaps.org/schemas/sitemap/0.9 http://www.sitemaps.org/schemas/sitemap/0.9/sitemap.xsd">
	<url>
		<loc blog:dloc="http://a.blog.ir/1">http://a.blog.ir/1</loc>
		<changefreq>weekly</changefreq>
		<priority>1.0</priority>
	</url>
	<url>
		<loc blog:dloc="http://a.blog.ir/2">http://a.blog.ir/2</loc>
		<changefreq>weekly</changefreq>
		<priority>1.0</priority>
	</url>
	
</urlset>
```

```xml
<document url="sitemap.xml" engine="xpath">
	<outlinks>
		<for-each root="dns:url"> 
			<link>
				<href>
					<text>
						<expr value="./dns:loc" />
					</text>
				</href>
				<anchor>
					<text>
						<expr value="./dns:loc/@blog:dloc" />
					</text>
				</anchor>
			</link>
		</for-each>
	</outlinks>
</document>
```

#### Using without nutch (standalone)
(TODO)

###Troubleshooting
(TODO)


	

