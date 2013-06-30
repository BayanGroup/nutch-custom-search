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

