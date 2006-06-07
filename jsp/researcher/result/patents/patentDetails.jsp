<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>

<span class="error"><html:errors/></span>
<html:messages id="message" message="true" bundle="RESEARCHER_RESOURCES">
	<span class="error">
		<bean:write name="message"/>
	</span>
</html:messages>

<logic:present role="RESEARCHER">
	<h2><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.patent.patentsManagement.title"/></h2>
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.result.patent.patentDetailsUseCase.title"/></h3>

	<%-- Authorships --%>
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="label.resultAuthorships"/></h3>
	<fr:view name="patent" property="resultAuthorships" schema="result.authorships" layout="tabular">
		<fr:layout>
			<fr:property name="sortBy" value="authorOrder"/>
		</fr:layout>
	</fr:view>
	<br/>
	
	<%-- Data --%>		
	<h3><bean:message bundle="RESEARCHER_RESOURCES" key="label.data"/></h3>
	<fr:view name="patent" schema="patent.viewEditData">
	    <fr:layout name="tabular">
    	    <fr:property name="classes" value="style1"/>
        	<fr:property name="columnClasses" value="listClasses,,"/>
	    </fr:layout>
	</fr:view>
	
	<br/>	
	<br/>
	<html:link page="<%= "/result/patents/patentsManagement.do?method=listPatents" %>">
		<bean:message bundle="RESEARCHER_RESOURCES" key="researcher.project.editProject.goBackToView" />
	</html:link>
</logic:present>
<br/>