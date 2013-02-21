<?xml version="1.0" encoding="UTF-8"?>
<!-- Copyright (C) 2011-2012 Oleg Tolmatcev <oleg_tolmatcev@yahoo.de> -->
<xsl:stylesheet
    version="1.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:exsl="http://exslt.org/common"
    xmlns:my="my"
    exclude-result-prefixes="my">

    <xsl:output method="text"/>

    <!-- 'ger': German, 'eng': English, 'rus', Russian, 'fre': French -->
    <xsl:param name="lang" select="'eng'"/>

    <xsl:key name="pos" match="my:part" use="@key"/>

    <xsl:template match="/">
        <xsl:apply-templates select="DslEntry"/>
    </xsl:template>

    <xsl:template match="DslEntry">
        <xsl:if test="sense/gloss">
            <xsl:for-each select="index">
                <xsl:value-of select="."/>
                <xsl:if test="position() != last()">
                    <xsl:text>&#x0A;</xsl:text><!-- \n -->
                </xsl:if>
            </xsl:for-each>
            <xsl:text>&#x0A;&#x09;</xsl:text><!-- \n\t -->
            <xsl:text>[m1]</xsl:text>
            <xsl:apply-templates select="entry"/>
            <xsl:text>[/m]</xsl:text>
            <xsl:text>&#x0A;&#x09;</xsl:text><!-- \n\t -->
            <xsl:text>[trn]</xsl:text>
            <xsl:call-template name="format-sense">
                <xsl:with-param name="sense" select="sense"/>
            </xsl:call-template>
            <xsl:text>[/trn]</xsl:text>
            <xsl:text>&#x0A;&#x0A;</xsl:text><!-- \n\n -->
        </xsl:if>
    </xsl:template>

    <xsl:template match="entry">
        <xsl:text>[c maroon]</xsl:text>
        <xsl:value-of select="kana"/>
        <xsl:text>[/c]</xsl:text>
        <xsl:if test="kanji/text()">
            <xsl:text>[c navy]&#x3010;</xsl:text>
            <xsl:value-of select="kanji"/>
            <xsl:text>&#x3011;[/c]</xsl:text>
        </xsl:if>
        <xsl:call-template name="format-info">
            <xsl:with-param name="info" select="info"/>
        </xsl:call-template>
        <xsl:if test="position() != last()">
            <xsl:text>、</xsl:text>
        </xsl:if>
    </xsl:template>

    <xsl:template name="format-info">
        <xsl:param name="info"/>
        <xsl:if test="$info">
            <xsl:text>[i](</xsl:text>
            <xsl:for-each select="$info">
                <xsl:variable name="pos" select="."/>
                <xsl:text>[p]</xsl:text>
                <xsl:for-each select="document('')">
                    <xsl:value-of select="key('pos', $pos)/@value"/>
                </xsl:for-each>
                <xsl:text>[/p]</xsl:text>
                <xsl:if test="position() != last()">,</xsl:if>
            </xsl:for-each>
            <xsl:text>)[/i]</xsl:text>
        </xsl:if>
    </xsl:template>

    <xsl:template name="format-lsource">
        <xsl:param name="lsource"/>
        <xsl:for-each select="$lsource">
            <xsl:choose>
                <xsl:when test="text()">
                    <xsl:text>[i]([p]</xsl:text>
                    <xsl:value-of select="@lang"/>
                    <xsl:text>[/p]: </xsl:text>
                    <xsl:value-of select="."/>
                    <xsl:text>)[/i]</xsl:text>
                </xsl:when>
                <xsl:otherwise>
                    <xsl:text>[i]([p]</xsl:text>
                    <xsl:value-of select="@lang"/>
                    <xsl:text>[/p])[/i]</xsl:text>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:if test="position() != last()">
                <xsl:text> </xsl:text>
            </xsl:if>
        </xsl:for-each>
    </xsl:template>

    <xsl:template name="format-gloss">
        <xsl:param name="gloss"/>
        <xsl:for-each select="$gloss">
            <xsl:value-of select="."/>
            <xsl:if test="position() != last()">; </xsl:if>
        </xsl:for-each>
        <xsl:if test="$gloss">
            <xsl:text>.</xsl:text>
        </xsl:if>
    </xsl:template>

    <xsl:template name="format-xref">
        <xsl:param name="xref"/>
        <xsl:if test="$xref">
            <xsl:text>(See </xsl:text>
            <xsl:for-each select="$xref">
                <xsl:text>[ref]</xsl:text>
                <xsl:value-of select="."/>
                <xsl:text>[/ref]</xsl:text>
                <xsl:if test="position() != last()">, </xsl:if>
            </xsl:for-each>
            <xsl:text>)</xsl:text>
        </xsl:if>
    </xsl:template>

    <xsl:template name="format-sense">
        <xsl:param name="sense"/>
        <xsl:variable name="count" select="count($sense[gloss])"/>
        <xsl:for-each select="$sense[gloss]">
            <xsl:text>[m2]</xsl:text>
            <xsl:if test="$count > 1">
                <xsl:number format="1) " value="position()"/>
            </xsl:if>
            <xsl:variable name="attributes">
                <attr>
                    <xsl:call-template name="format-info">
                        <xsl:with-param name="info" select="pos"/>
                    </xsl:call-template>
                </attr>

                <attr>
                    <xsl:call-template name="format-info">
                        <xsl:with-param name="info" select="field"/>
                    </xsl:call-template>
                </attr>

                <attr>
                    <xsl:call-template name="format-info">
                        <xsl:with-param name="info" select="misc"/>
                    </xsl:call-template>
                </attr>

                <attr>
                    <xsl:call-template name="format-info">
                        <xsl:with-param name="info" select="dial"/>
                    </xsl:call-template>
                </attr>

                <attr>
                    <xsl:call-template name="format-lsource">
                        <xsl:with-param name="lsource" select="lsource"/>
                    </xsl:call-template>
                </attr>

                <attr>
                    <xsl:call-template name="format-gloss">
                        <xsl:with-param name="gloss" select="gloss"/>
                    </xsl:call-template>
                </attr>

                <attr>
                    <xsl:call-template name="format-xref">
                        <xsl:with-param name="xref" select="xref"/>
                    </xsl:call-template>
                </attr>
            </xsl:variable>

            <xsl:for-each select="exsl:node-set($attributes)/attr[text()]">
                <xsl:value-of select="."/>
                <xsl:if test="position() != last()">
                    <xsl:text> </xsl:text>
                </xsl:if>
            </xsl:for-each>

            <xsl:text>[/m]</xsl:text>
            <xsl:if test="position() != last()">
                <xsl:text>&#x0A;&#x09;</xsl:text>
            </xsl:if>
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
        <my:part key="old or irregular kana form" value="oik">
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
        <my:part key="Yodan verb with `hu/fu' ending (archaic)" value="v4h"/>
        <my:part key="Yodan verb with `ru' ending (archaic)" value="v4r"/>
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
        <my:part key="`kari' adjective (archaic)" value="adj-kari"/>
        <my:part key="`ku' adjective (archaic)" value="adj-ku"/>
        <my:part key="`shiku' adjective (archaic)" value="adj-shiku"/>
        <my:part key="archaic/formal form of na-adjective" value="adj-nari"/>
        <my:part key="proper noun" value="n-pr"/>
        <my:part key="verb unspecified" value="v-unspec"/>
        <my:part key="Yodan verb with `ku' ending (archaic)" value="v4k"/>
        <my:part key="Yodan verb with `gu' ending (archaic)" value="v4g"/>
        <my:part key="Yodan verb with `su' ending (archaic)" value="v4s"/>
        <my:part key="Yodan verb with `tsu' ending (archaic)" value="v4t"/>
        <my:part key="Yodan verb with `nu' ending (archaic)" value="v4n"/>
        <my:part key="Yodan verb with `bu' ending (archaic)" value="v4b"/>
        <my:part key="Yodan verb with `mu' ending (archaic)" value="v4m"/>
        <my:part key="Nidan verb (upper class) with `ku' ending (archaic)" value="v2k-k"/>
        <my:part key="Nidan verb (upper class) with `gu' ending (archaic)" value="v2g-k"/>
        <my:part key="Nidan verb (upper class) with `tsu' ending (archaic)" value="v2t-k"/>
        <my:part key="Nidan verb (upper class) with `dzu' ending (archaic)" value="v2d-k"/>
        <my:part key="Nidan verb (upper class) with `hu/fu' ending (archaic)" value="v2h-k"/>
        <my:part key="Nidan verb (upper class) with `bu' ending (archaic)" value="v2b-k"/>
        <my:part key="Nidan verb (upper class) with `mu' ending (archaic)" value="v2m-k"/>
        <my:part key="Nidan verb (upper class) with `yu' ending (archaic)" value="v2y-k"/>
        <my:part key="Nidan verb (upper class) with `ru' ending (archaic)" value="v2r-k"/>
        <my:part key="Nidan verb (lower class) with `ku' ending (archaic)" value="v2k-s"/>
        <my:part key="Nidan verb (lower class) with `gu' ending (archaic)" value="v2g-s"/>
        <my:part key="Nidan verb (lower class) with `su' ending (archaic)" value="v2s-s"/>
        <my:part key="Nidan verb (lower class) with `zu' ending (archaic)" value="v2z-s"/>
        <my:part key="Nidan verb (lower class) with `tsu' ending (archaic)" value="v2t-s"/>
        <my:part key="Nidan verb (lower class) with `dzu' ending (archaic)" value="v2d-s"/>
        <my:part key="Nidan verb (lower class) with `nu' ending (archaic)" value="v2n-s"/>
        <my:part key="Nidan verb (lower class) with `hu/fu' ending (archaic)" value="v2h-s"/>
        <my:part key="Nidan verb (lower class) with `bu' ending (archaic)" value="v2b-s"/>
        <my:part key="Nidan verb (lower class) with `mu' ending (archaic)" value="v2m-s"/>
        <my:part key="Nidan verb (lower class) with `yu' ending (archaic)" value="v2y-s"/>
        <my:part key="Nidan verb (lower class) with `ru' ending (archaic)" value="v2r-s"/>
        <my:part key="Nidan verb (lower class) with `u' ending and `we' conjugation (archaic)" value="v2w-s"/>
        <my:part key="architecture term" value="archit"/>
        <my:part key="anatomical term" value="anat"/>
        <my:part key="astronomy, etc. term" value="astron"/>
        <my:part key="baseball term" value="baseb"/>
        <my:part key="biology term" value="biol"/>
        <my:part key="botany term" value="bot"/>
        <my:part key="business term" value="bus"/>
        <my:part key="economics term" value="econ"/>
        <my:part key="engineering term" value="engr"/>
        <my:part key="finance term" value="finc"/>
        <my:part key="geology, etc. term" value="geol"/>
        <my:part key="law, etc. term" value="law"/>
        <my:part key="medicine, etc. term" value="med"/>
        <my:part key="music term" value="music"/>
        <my:part key="Shinto term" value="Shinto"/>
        <my:part key="sports term" value="sports"/>
        <my:part key="sumo term" value="sumo"/>
        <my:part key="zoology term" value="zool"/>
        <my:part key="jocular, humorous term" value="joc"/>
    </my:parts>
</xsl:stylesheet>
