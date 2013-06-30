nutch-custom-search
===================

The goal of this project is to develope plugins/extentions for nutch to make it a perfect tool for building custom search solutions for dirrerent site.

Extractor
---------

Extractor helps to extract different parts of a html/xml page using xpath expressioins or css selectors. 
The extracted parts are copied into the specified fields of documents.
This enables to have different docuements for different pages each with their own fields.
Next, one can tune solr to search over these fields with appropriate boosts, which results to better search experience for end users.
