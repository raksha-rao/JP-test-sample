<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:output method="xml" indent="yes"/>
  <xsl:strip-space elements="*"/>

  <xsl:template match="node()[not(self::comment())][not(self::*)]|@*">
    <xsl:copy>
      <xsl:apply-templates select="node()|@*"/>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="*">
    <xsl:copy>
      <xsl:apply-templates select="@*">
        <xsl:sort select="name()"/>
      </xsl:apply-templates>
      <xsl:apply-templates>
        <xsl:sort select="name()"/>
      </xsl:apply-templates>
    </xsl:copy>
  </xsl:template>
  
  <xsl:template match="//payment_data">
    <xsl:copy>
      <xsl:apply-templates select="item">
        <xsl:sort select="payment_side_code" order="ascending"/>
      </xsl:apply-templates>
    </xsl:copy>
  </xsl:template>
  
  <xsl:template match="//payment_data/item/payments_data">
    <xsl:copy>
      <xsl:apply-templates select="item">
        <xsl:sort select="type_code" order="ascending"/>
      </xsl:apply-templates>
    </xsl:copy>
  </xsl:template>
  
</xsl:stylesheet>