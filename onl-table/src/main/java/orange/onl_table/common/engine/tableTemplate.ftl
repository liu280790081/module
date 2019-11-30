<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE hibernate-mapping PUBLIC "-//Hibernate/Hibernate Mapping DTD 3.0//EN" "http://hibernate.sourceforge.net/hibernate-mapping-3.0.dtd">
<hibernate-mapping>
    <class name="${head.tableName}" table="${head.tableName}" optimistic-lock="version">
        <comment>${head.tableTxt}</comment>
		<#if columns?exists>
            <#list columns as attr>
                <#if attr.isKey == 1>
                    <id unsaved-value="null" name="${attr.columnDialect}" length="${attr.columnLength}"
                    <#switch attr.columnType?lower_case>
                        <#case "string"> type="java.lang.String" <#break>
                        <#default> type="java.lang.Long" <#break>
                    </#switch>>
                    <#switch attr.columnType?lower_case>
                        <#case "string"> <generator class="uuid" /> <#break>
                        <#default> <generator class="identity" /> <#break>
                    </#switch>
                    </id>
                <#else>
					<property access="property" name="${attr.columnDialect}" <#switch attr.columnType?lower_case>
                        <#case "string"> type="java.lang.String" <#break>
                        <#case "text"> type="text" <#break>
                        <#case "int"> type="java.lang.Integer" <#break>
                        <#case "double"> type="java.lang.Double" <#break>
                        <#case "date">
                        <#case "datetime"> type="java.util.Date" <#break>
                        <#case "decimal"> type="java.math.BigDecimal" <#break>
                        <#case "blob"> type="blob" <#break>
                    </#switch>>
                        <column name="${attr.columnDialect}"
                        <#if attr.columnType=='double'|| attr.columnType=='decimal' || attr.columnType=='bigdecimal'>
                            precision="${attr.columnLength}" scale="${attr.numericScale}"
                        <#else>
                            length="${attr.columnLength}"
                        </#if>
                        <#if attr.columnDefaultVal?exists&&attr.columnDefaultVal!=''>
                            default="${attr.columnDefaultVal}"
                        </#if>
                            not-null="<#if attr.isNullable == 1>false<#else>true</#if>" unique="false">
                            <comment>${attr.columnComment}</comment>
                        </column>
                    </property>
                </#if>
            </#list>
        </#if>
    </class>
</hibernate-mapping>