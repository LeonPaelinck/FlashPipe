<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="3.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:xs="http://www.w3.org/2001/XMLSchema" xmlns:fn="http://www.w3.org/2005/xpath-functions" xmlns:multimap="http://sap.com/xi/XI/SplitAndMerge">
	<xsl:template match="/">
		<genroot>
			<xsl:for-each select="/multimap:Messages/multimap:Message1/FOCostCenter/FOCostCenter">
				<root>
					<xsl:copy-of select="."/>
					<xsl:variable name="a" select="(./externalCode)"/>
					<xsl:copy-of select="/multimap:Messages/multimap:Message2/EmpJob/EmpJob[./costCenterNav/FOCostCenter/externalCode = $a][./customDouble1 != '']"/>
				</root>
			</xsl:for-each>
		</genroot><!-- TODO: Auto-generated template -->
	</xsl:template>
</xsl:stylesheet>
