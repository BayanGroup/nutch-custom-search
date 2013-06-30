nutch-custom-search
===================

The goal of this project is to develope plugins/extentions for nutch to make it a perfect tool for building custom search solutions for dirrerent sites.

Extractor
---------

Extractor helps to extract different parts of a html/xml page using xpath expressioins or css selectors. 
The extracted parts are copied into the specified fields of documents.
This enables to have different docuements for different pages each with their own fields.
Next, one can tune solr to search over these fields with appropriate boosts, which results to better search experience for end users.

Extractor consists of a parser plugin, an html parser filter, and an indexer filter.

### Setup

1. Copy plugins/extractor to your nutch's plugins directory.
2. Enable this plugin by adding its name to plugin.includes property in the nutch-site.xml. nutch-site.xml is inside nutch conf directory. You can disable other parser/indexer plugins since extractor already contains both a parser and an indexer. e.g. you can use this:

```xml
<property>
  <name>plugin.includes</name>
  <value>protocol-http|urlfilter-regex|extractor|scoring-opic|urlnormalizer-(pass|regex|basic)</value>
</property>
```

3. Introduce the extractor parser to nutch by modifiying the parse-plugins.xml file. This file is inside nutch conf directory. First, add its alias in the aliases section:

```xml
<aliases>
	...
	<alias name="extractor" extension-id="ir.co.bayan.simorq.zal.extractor.nutch.ExtractorParser" />
</aliases>
```

Next, if you want the extractor parse all content types, add it to the mimeType with name="*":

```xml
<mimeType name="*">
	<plugin id="extractor" />
	<plugin id="parse-tika" />
</mimeType>

```

Or if you want to use it for specific content types add its name to the corresponding mimeType:

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


