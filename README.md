nutch-custom-search
===================

The goal of this project is to develope plugins/extentions for nutch to make it a perfect tool for building custom search solutions for dirrerent sites.

Extractor
---------

Extractor helps to extract different parts of an html/xml page using xpath expressioins or css selectors. 
The extracted parts are copied into the specified fields of documents.
This enables to have different docuements for different pages each with their own fields.
Next, one can instruct solr to search over these fields with appropriate boosts, which results to better search experience for end users.

Extractor consists of a parser plugin, an html parser filter, and an indexer filter. You can use extractor in two modes:

1. Use extractor to extract additional fields inaddition of the standard html parser fields (such as title, metatags, outlinks,...). In this mode, extractor is attached to the standard html parser as a parser filter. So html parser first parses the content, extracts its fields and outlinks, and then passes the content to the extractor parser filter. Extractor filter, parses the content again and adds some additional fields to parse metadata. Later, the extractor indexer filter reads this parse metadata and adds the corresponding fields to solr documents. 

2. Use extractor as a standalone parser. In this mode, extractor is responsible for the whole parsing process including the extraction of title, outlinks, ... You can use extractor to parse both xml and html files. The available index filters in the nutch has little use, since standard fields are not available any more. 

### Setup

1) Copy plugins/extractor to your nutch's plugins directory.

2) Enable extractor plugin by adding its name to plugin.includes property in the nutch-site.xml (inside nutch conf directory). If you use mode 2, you can disable other parser/indexer plugins e.g. you can use this:

```xml
<property>
  <name>plugin.includes</name>
  <value>protocol-http|urlfilter-regex|extractor|scoring-opic|urlnormalizer-(pass|regex|basic)</value>
</property>
```

3) If you use mode 2, you should introduce the extractor parser to nutch by modifiying the parse-plugins.xml (inside nutch conf directory). First, add its alias in the aliases section:

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

4) Add your extractors.xml to nutch conf directory. A simple extractors.xml is look like this:

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
2. fields: contains all fields that extracted data should be put into them. These fields should be define in the solrschema.xml file too to be recongnizable by solr. Each field have an optional type which is one of the types defined in the types section.
3. documents: contains one or several documents. For each resource (e.g. web page) that you want to extract its content into fields you need to define a document. When a resource with a specific url and some content is fetched by nutch, 
the extractor looks for a docuemnt that matches the resoruce url. Each docuement declares its accepting urls by means of regex expressions. If multiple matching documents are fround by extractor, the first one will be used. 
Then, the contnet of the resource is parsed using an engine that specified by the dccument (or using default engine if no engine specified). Currently there are three engines avaialbe: 
css (which parses the content using jsoup library and is able to answer css selectors expressions), 
xpath (which uses the standard JAXP infrastrucutre and is able to answer xpath expressions),
txt (suitable for line oriented processing of text files). 
Each document consits of a set of extract-to rules. An extract-to rule extract a value from the content and put the extracted value into a field which is one of the fields defined in the fields section. The value of the field is extracted by means of functions. 

Here is a very simple extractors.xml file containing all of the above sections:

```xml
<config xmlns="http://bayan.ir" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://bayan.ir http://raw.github.com/BayanGroup/nutch-custom-search/master/zal.extractor/src/main/resources/extractors.xsd">
	<types>
		<type name="long" converter="ir.co.bayan.simorq.zal.extractor.convert.LongConverter" />
	</types>
	<fields>
		<field name="num-items" type="long" />
	</fields>
	<documents>
		<document url="^http://.+?\.google\.com" engine="css">
			<extract-to field="num-items">
				<size>
					<expr value="li.gbt" />
				</size>
			</extract-to>
		</document>
	</documents>
</config>
```

The purpose of this file is to extract the number of itmes in the topmost bar of google web page and put it in a field with name num-items.
In this file, we have one type named long with a converter. This converter is used to convert the extracted value (a string) to desired type.
Also we have a field named "num-items".
In the documents section, we defined a document wich accepts all resoruces with url ending with .google.com. The document specifies that its content should be parsed using css engine.
This document has only one extract-to rule which consists of two nested functions size and expr. 
The expr function returens a set of objects by quering the content using the provided engine. Here since our engine is css, li.gbt means all li elements with class .gbt.
The size function, returnes the number of its argument which here is the list of elements that satisfy li.gbt expression.
This extracted value is copied into field named "num-items".

### Functions

The following table lists available functions which can be used in extract-to rules. 

Function name | Input | Output | Description
------------- | ----- | ------ | -----------
attribute | | | Extracts the value of attribute with specified name from input objects.
concat | | | Concats its inputs by the provided delimiter.
constant | | |
expr | | | Evaluates an expression using current engine.
first | | |
for-each | | |  Iterates through its children with the given root as the new root.
last | | |
link | | |
replace | | | Replaces its input using the provided regex pattern by the provided substitution.
resolve | | | Resolves a possible relative url to absolute one based on the current url in the context.
size | | |
text | | | Returns the text content of its input.
trancate | | | Trancates a string if its size is greater than max.
trim | | | Trims a string.
url | | | Returns the current url in the context.



