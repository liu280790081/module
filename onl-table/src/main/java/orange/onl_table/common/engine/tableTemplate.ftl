<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE hibernate-mapping PUBLIC "-//Hibernate/Hibernate Mapping DTD 3.0//EN" "http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd">
<hibernate-mapping>
    <class name="${entity.tableName}" table="${entity.tableName}" optimistic-lock="version">
        <comment>${entity.comment}</comment>
		<#if entity.columns?exists>
            <#list entity.columns as attr>
                <#if attr.dbIsKey == 1>
                    <id unsaved-value="null" name="${attr.dbFieldName}" length="${attr.dbLength}" <#switch attr.dbType?lower_case>
                        <#case "string"> type="java.lang.String" <#break>
                        <#default> type="java.lang.Long" <#break>
                    </#switch>>
                    <#switch attr.dbType?lower_case>
                        <#case "string"> <generator class="uuid" /> <#break>
                        <#default> <generator class="identity" /> <#break>
                    </#switch>
                    </id>
                <#else>
					<property access="property" name="${attr.dbFieldName}" <#switch attr.dbType?lower_case>
                        <#case "string"> type="java.lang.String" <#break>
                        <#case "text"> type="text" <#break>
                        <#case "int"> type="java.lang.Integer" <#break>
                        <#case "double"> type="java.lang.Double" <#break>
                        <#case "date">
                        <#case "datetime"> type="java.util.Date" <#break>
                        <#case "decimal"> type="java.math.BigDecimal" <#break>
                        <#case "blob"> type="blob" <#break>
                    </#switch>>
                        <column name="`${attr.dbFieldName}`"
                        <#if attr.dbType=='double'|| attr.dbType=='decimal' || attr.dbType=='bigdecimal'>
                            precision="${attr.dbLength}" scale="${attr.dbPointLength}"
                        <#else>
                            length="${attr.dbLength}"
                        </#if>
                        <#if attr.dbDefaultVal?exists&&attr.dbDefaultVal!=''>
                            default="${attr.dbDefaultVal}"
                        </#if>
                            not-null="<#if attr.dbIsNull == 1>false<#else>true</#if>" unique="false">
                            <comment>${attr.dbFieldTxt}</comment>
                        </column>
                    </property>
                </#if>
            </#list>
        </#if>
    </class>
</hibernate-mapping>