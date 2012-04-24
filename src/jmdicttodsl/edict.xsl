<?xml version="1.0"?>
<!-- Copyright (C) 2011-2012 Oleg Tolmatcev <oleg_tolmatcev@yahoo.de> -->
<xsl:stylesheet
xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
version="1.0"
xmlns:my="my"
exclude-result-prefixes="my">
    <!--
    Converts the JMdict dictionary to the EDICT format.
    -->
    <xsl:output method="text"/>

    <!-- 'ger': German, 'eng': English, 'rus', Russian, 'fre': French -->
    <xsl:param name="lang" select="'eng'"/>

    <xsl:key name="pos" match="my:part" use="@key"/>

    <xsl:strip-space elements="*"/>

    <xsl:template match="/">
        <xsl:apply-templates select="entry"/>
    </xsl:template>

    <xsl:template match="entry">
        <xsl:variable name="entry" select="."/>
        <xsl:for-each select="$entry/r__ele">
            <xsl:variable name="r__ele" select="."/>
            <xsl:choose>
                <xsl:when test="$entry/k__ele">
                    <xsl:for-each select="$entry/k__ele">
                        <xsl:variable name="k__ele" select="."/>
                        <xsl:call-template name="process-entry">
                            <xsl:with-param name="entry" select="$entry"/>
                            <xsl:with-param name="k__ele" select="$k__ele"/>
                            <xsl:with-param name="r__ele" select="$r__ele"/>
                        </xsl:call-template>
                    </xsl:for-each>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:call-template name="process-entry">
                        <xsl:with-param name="entry" select="$entry"/>
                        <xsl:with-param name="k__ele" select="$entry/k__ele"/><!-- empty node-set -->
                        <xsl:with-param name="r__ele" select="$r__ele"/>
                    </xsl:call-template>
                </xsl:otherwise>
            </xsl:choose>
        </xsl:for-each>
    </xsl:template>

    <xsl:template name="process-entry">
        <xsl:param name="entry"/>
        <xsl:param name="k__ele"/>
        <xsl:param name="r__ele"/>

        <xsl:if test="($r__ele/re__restr=$k__ele/keb) or not($r__ele/re__restr)">
            <xsl:variable name="sense"
            select="$entry/sense[((not(stagr) and not(stagk))
            or stagr=$r__ele/reb or stagk=$k__ele/keb)]"/>

            <xsl:if test="$sense">
                <xsl:choose>
                    <xsl:when test="$k__ele">
                        <xsl:value-of select="$k__ele/keb"/> [<xsl:value-of select="$r__ele/reb"/><xsl:text>] /</xsl:text>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="$r__ele/reb"/><xsl:text> /</xsl:text>
                    </xsl:otherwise>
                </xsl:choose>

                <xsl:call-template name="info">
                    <xsl:with-param name="nodes" select="$k__ele/ke__inf"/>
                </xsl:call-template>

                <xsl:call-template name="info">
                    <xsl:with-param name="nodes" select="$r__ele/re__inf"/>
                </xsl:call-template>

                <xsl:call-template name="sense">
                    <xsl:with-param name="nodes" select="$sense"/>
                </xsl:call-template>

                <xsl:text>&#xA;</xsl:text><!-- '\n' -->
            </xsl:if>
        </xsl:if>
    </xsl:template>

    <xsl:template name="sense">
        <xsl:param name="nodes"/>
        <xsl:for-each select="$nodes">
            <xsl:call-template name="info">
                <xsl:with-param name="nodes" select="pos"/>
            </xsl:call-template>

            <xsl:if test="count($nodes) > 1">
                <xsl:number format="(1) " value="position()"/>
            </xsl:if>

            <xsl:call-template name="info">
                <xsl:with-param name="nodes" select="field"/>
            </xsl:call-template>

            <xsl:call-template name="info">
                <xsl:with-param name="nodes" select="misc"/>
            </xsl:call-template>

            <xsl:call-template name="lsource">
                <xsl:with-param name="nodes" select="lsource"/>
            </xsl:call-template>

            <xsl:call-template name="dial">
                <xsl:with-param name="nodes" select="dial"/>
            </xsl:call-template>

            <xsl:for-each select="gloss">
                <xsl:value-of select="."/><xsl:text>/</xsl:text>
            </xsl:for-each>
        </xsl:for-each>
    </xsl:template>

    <xsl:template name="info">
        <xsl:param name="nodes"/>
        <xsl:if test="$nodes">
            <xsl:text>(</xsl:text>
            <xsl:for-each select="$nodes">
                <xsl:variable name="node" select="."/>
                <xsl:for-each select="document('')">
                    <xsl:value-of select="key('pos', $node)/@value"/>
                </xsl:for-each>
                <xsl:if test="last() != position()">,</xsl:if>
            </xsl:for-each>
            <xsl:text>) </xsl:text>
        </xsl:if>
    </xsl:template>

    <xsl:template name="lsource">
        <xsl:param name="nodes"/>
        <xsl:for-each select="$nodes">
            <xsl:text>(</xsl:text>
            <xsl:value-of select="@lang"/>
            <xsl:text>: </xsl:text>
            <xsl:value-of select="."/>
            <xsl:text>) </xsl:text>
        </xsl:for-each>
    </xsl:template>

    <xsl:template name="dial">
        <xsl:param name="nodes"/>
        <xsl:for-each select="$nodes">
            <xsl:text>(</xsl:text>
            <xsl:value-of select="."/>
            <xsl:text>:) </xsl:text>
        </xsl:for-each>
    </xsl:template>

    <my:parts>
        <my:part key="martial arts term" value="MA"/>
        <my:part key="rude or X-rated term (not displayed in educational software)" value="X"/>
        <my:part key="abbreviation" value="abbr"/>
        <my:part key="adjective (keiyoushi)" value="adj-i"/>
        <my:part key="adjectival nouns or quasi-adjectives (keiyodoshi)" value="adj-na"/>
        <my:part key="nouns which may take the genitive case particle `no'" value="adj-no"/>
        <my:part key="pre-noun adjectival (rentaishi)" value="adj-pn"/>
        <my:part key="`taru' adjective" value="adj-t"/>
        <my:part key="noun or verb acting prenominally" value="adj-f"/>
        <my:part key="former adjective classification (being removed)" value="adj"/>
        <my:part key="adverb (fukushi)" value="adv"/>
        <my:part key="adverb taking the `to' particle" value="adv-to"/>
        <my:part key="archaism" value="arch"/>
        <my:part key="ateji (phonetic) reading" value="ateji"/>
        <my:part key="auxiliary" value="aux"/>
        <my:part key="auxiliary verb" value="aux-v"/>
        <my:part key="auxiliary adjective" value="aux-adj"/>
        <my:part key="Buddhist term" value="Buddh"/>
        <my:part key="chemistry term" value="chem"/>
        <my:part key="children's language" value="chn"/>
        <my:part key="colloquialism" value="col"/>
        <my:part key="computer terminology" value="comp"/>
        <my:part key="conjunction" value="conj"/>
        <my:part key="counter" value="ctr"/>
        <my:part key="derogatory" value="derog"/>
        <my:part key="exclusively kanji" value="eK"/>
        <my:part key="exclusively kana" value="ek"/>
        <my:part key="Expressions (phrases, clauses, etc.)" value="exp"/>
        <my:part key="familiar language" value="fam"/>
        <my:part key="female term or language" value="fem"/>
        <my:part key="food term" value="food"/>
        <my:part key="geometry term" value="geom"/>
        <my:part key="gikun (meaning as reading)  or jukujikun (special kanji reading)" value="gikun"/>
        <my:part key="honorific or respectful (sonkeigo) language" value="hon"/>
        <my:part key="humble (kenjougo) language" value="hum"/>
        <my:part key="word containing irregular kanji usage" value="iK"/>
        <my:part key="idiomatic expression" value="id"/>
        <my:part key="word containing irregular kana usage" value="ik"/>
        <my:part key="interjection (kandoushi)" value="int"/>
        <my:part key="irregular okurigana usage" value="io"/>
        <my:part key="irregular verb" value="iv"/>
        <my:part key="linguistics terminology" value="ling"/>
        <my:part key="manga slang" value="m-sl"/>
        <my:part key="male term or language" value="male"/>
        <my:part key="male slang" value="male-sl"/>
        <my:part key="mathematics" value="math"/>
        <my:part key="military" value="mil"/>
        <my:part key="noun (common) (futsuumeishi)" value="n"/>
        <my:part key="adverbial noun (fukushitekimeishi)" value="n-adv"/>
        <my:part key="noun, used as a suffix" value="n-suf"/>
        <my:part key="noun, used as a prefix" value="n-pref"/>
        <my:part key="noun (temporal) (jisoumeishi)" value="n-t"/>
        <my:part key="numeric" value="num"/>
        <my:part key="word containing out-dated kanji" value="oK"/>
        <my:part key="obsolete term" value="obs"/>
        <my:part key="obscure term" value="obsc"/>
        <my:part key="out-dated or obsolete kana usage" value="ok"/>
        <my:part key="onomatopoeic or mimetic word" value="on-mim"/>
        <my:part key="pronoun" value="pn"/>
        <my:part key="poetical term" value="poet"/>
        <my:part key="polite (teineigo) language" value="pol"/>
        <my:part key="prefix" value="pref"/>
        <my:part key="proverb" value="proverb"/>
        <my:part key="particle" value="prt"/>
        <my:part key="physics terminology" value="physics"/>
        <my:part key="rare" value="rare"/>
        <my:part key="sensitive" value="sens"/>
        <my:part key="slang" value="sl"/>
        <my:part key="suffix" value="suf"/>
        <my:part key="word usually written using kanji alone" value="uK"/>
        <my:part key="word usually written using kana alone" value="uk"/>
        <my:part key="Ichidan verb" value="v1"/>
        <my:part key="Nidan verb with 'u' ending (archaic)" value="v2a-s"/>
        <my:part key="Yondan verb with `hu/fu' ending (archaic)" value="v4h"/>
        <my:part key="Yondan verb with `ru' ending (archaic)" value="v4r"/>
        <my:part key="Godan verb (not completely classified)" value="v5"/>
        <my:part key="Godan verb - -aru special class" value="v5aru"/>
        <my:part key="Godan verb with `bu' ending" value="v5b"/>
        <my:part key="Godan verb with `gu' ending" value="v5g"/>
        <my:part key="Godan verb with `ku' ending" value="v5k"/>
        <my:part key="Godan verb - Iku/Yuku special class" value="v5k-s"/>
        <my:part key="Godan verb with `mu' ending" value="v5m"/>
        <my:part key="Godan verb with `nu' ending" value="v5n"/>
        <my:part key="Godan verb with `ru' ending" value="v5r"/>
        <my:part key="Godan verb with `ru' ending (irregular verb)" value="v5r-i"/>
        <my:part key="Godan verb with `su' ending" value="v5s"/>
        <my:part key="Godan verb with `tsu' ending" value="v5t"/>
        <my:part key="Godan verb with `u' ending" value="v5u"/>
        <my:part key="Godan verb with `u' ending (special class)" value="v5u-s"/>
        <my:part key="Godan verb - Uru old class verb (old form of Eru)" value="v5uru"/>
        <my:part key="Godan verb with `zu' ending" value="v5z"/>
        <my:part key="Ichidan verb - zuru verb (alternative form of -jiru verbs)" value="vz"/>
        <my:part key="intransitive verb" value="vi"/>
        <my:part key="Kuru verb - special class" value="vk"/>
        <my:part key="irregular nu verb" value="vn"/>
        <my:part key="irregular ru verb, plain form ends with -ri" value="vr"/>
        <my:part key="noun or participle which takes the aux. verb suru" value="vs"/>
        <my:part key="su verb - precursor to the modern suru" value="vs-c"/>
        <my:part key="suru verb - special class" value="vs-s"/>
        <my:part key="suru verb - irregular" value="vs-i"/>
        <my:part key="Kyoto-ben" value="kyb"/>
        <my:part key="Osaka-ben" value="osb"/>
        <my:part key="Kansai-ben" value="ksb"/>
        <my:part key="Kantou-ben" value="ktb"/>
        <my:part key="Tosa-ben" value="tsb"/>
        <my:part key="Touhoku-ben" value="thb"/>
        <my:part key="Tsugaru-ben" value="tsug"/>
        <my:part key="Kyuushuu-ben" value="kyu"/>
        <my:part key="Ryuukyuu-ben" value="rkb"/>
        <my:part key="Nagano-ben" value="nab"/>
        <my:part key="transitive verb" value="vt"/>
        <my:part key="vulgar expression or word" value="vulg"/>
    </my:parts>
</xsl:stylesheet>
