nutch-custom-search
===================

The goal of this project is to develope plugins/extentions for Nutch to make it a perfect tool for building custom search solutions. 
The project is an open-source project released under Apache License Version 2.0.

Extractor
---------

Extractor helps to extract specific parts of an html/xml page using xpath expressioins or css selectors. 
The extracted parts are copied into one or several fields.


Extractor consists of a parser plugin, an html parser filter, and an indexer filter. You can use extractor in two modes:

1. Use extractor to extract additional fields inaddition of the standard html parser fields (such as title, metatags, outlinks,...). In this mode, extractor is attached to the standard html parser as a parser filter. Html parser first parses the content, extracts its fields and outlinks, and then passes the content to the extractor parser filter. Extractor, parses the content again and adds some additional fields to the parse metadata. Later, the extractor indexer filter reads this parse metadata and adds the corresponding fields to solr documents. 

2. Use extractor as a standalone parser. In this mode, extractor is responsible for the whole parsing process including the extraction of title, outlinks, .... In this mode, you can use extractor to parse both xml and html files. 

### Setup

1) Copy plugins/extractor to your nutch's plugins directory. Note that the jar is built with java 7, so if you are using a lower version of java, you should compile it yourself by downloading the zal.extractor project and built it using maven.

2) Enable extractor plugin by adding its name to the plugin.includes property in the nutch-site.xml (inside nutch conf directory). If you are useing mode 2, you can disable other parser/indexer plugins e.g. you can use something like this:

```xml
<property>
  <name>plugin.includes</name>
  <value>protocol-http|urlfilter-regex|extractor|scoring-opic|urlnormalizer-(pass|regex|basic)</value>
</property>
```

3) If you are using mode 2, you should introduce the extractor parser to nutch by modifiying the parse-plugins.xml (inside nutch conf directory). First, add its alias in the aliases section:

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
2. fields: contains all the fields that the extracted parts should be put into them. These fields should be define in the solrschema.xml file too to be recongnizable by solr. Each field has an optional type which is one of the types defined in the types section.
3. documents: contains one or several documents. For each resource (e.g. a web page) that you want to extract its content into the fields, you need to define a document. Each docuement declares its accepting urls by means of regex expressions. When a resource with a specific url and some content is fetched by nutch, 
the extractor looks for a docuemnt that its url matches the resoruce url. If multiple matching documents are fround, the first one will be used. 
Then, the contnet of the resource is parsed using an engine that specified by the dccument (or using the default engine if no engine is specified). Currently there are three engines avaialbe: 
css (which parses the content using jsoup library and is able to answer css selectors expressions), 
xpath (which uses the standard JAXP infrastrucutre and is able to answer xpath expressions),
txt (suitable for line oriented processing of text files). 
Each document consits of a set of extract-to rules. An extract-to rule extracts a value from the content and put the extracted value into its defined field which is one of the fields defined in the fields section. The value of the field is extracted by means of functions. 
Each document may specifiy an outlinks section that tells extractor how outlinks should be extracted from the current content. Also each document can have an id and other documents can inherit its fields by specifying its id in their inherits attribute.

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
Also we have a field named "num-items" of type long and a field named "all-items" of type string.
In the documents section, we defined a document which accepts all resoruces with url ending with .google.com. Since the extractor uses the partial regex matching we could write this as "google\.com/?$" too. The document specifies that its content should be parsed using the css engine.
This document has two extract-to rules. The first rule consists of two nested functions size and expr. 
The "expr" function returens a set of objects by quering the content using the provided engine. Here since our engine is css, li.gbt means all li elements with class .gbt.
The size function, returnes the number of its argument which here is the list of elements that satisfy li.gbt expression.
This extracted value is copied into field named "num-items".
In the second rule, we first extract the text value of all li.gbt span.gbts nodes, then concat them with comma as the seperator and then limit their size to have less than 100 charcters. 

### Functions

The following table lists available functions which can be used in extract-to rules. 
All functions output a list of objects and takes as input a list of objects. Hence they can be nested (chained) to use output of one function as an input of another function.

Function name | Description
------------- | -----------
attribute | Extracts the value of attribute with specified name from input objects.
concat | Concats its inputs by the provided delimiter.
constant | Always returns a fixed constant.
decode | Decodes the given url string.
expr | Evaluates an expression using the current engine and returnes the list of result objects. The evaluation is done in the scope of current root. By default all document is the current root unless it is changed using for-each.
fetch | Fetches a content from the given url and evaluates it with the specified engine.
field-value | Returns the extracted value of the given field.
first | Returns the first object in the list of its argument.
for-each |  Iterates through its children with the given root as the new root.
last | Returns the last object in the list of its argument.
link  | Retunes a set of links with href and anchors.
replace | Replaces its input using the provided regex pattern by the provided substitution.
resolve | Resolves a possible relative url to absolute one based on the current url in the context.
size | Returns the number of objects in its argument (which is a list).
text  | Returns the text content of its input.
truncate | Truncates a string if its size is greater than max.
trim | Trims a string.
url | Returns the current url in the context.




