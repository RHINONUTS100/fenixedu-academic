<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr" %>

<html:xhtml/>

<logic:present role="ACADEMIC_ADMINISTRATIVE_OFFICE">

<em><bean:message key="label.academicAdminOffice" bundle="ACADEMIC_OFFICE_RESOURCES"/></em>
<h2><bean:message key="label.externalUnits.editExternalEnrolment" bundle="ACADEMIC_OFFICE_RESOURCES"/></h2>

<bean:define id="externalCurricularCourseId">&oid=<bean:write name="externalEnrolmentBean" property="externalCurricularCourse.idInternal" /></bean:define>

<html:messages property="error" message="true" id="errMsg" bundle="ACADEMIC_OFFICE_RESOURCES">
	<p>
		<span class="error0"><!-- Error messages go here --><bean:write name="errMsg" /></span>
	</p>
</html:messages>

<bean:define id="contextInformation" name="contextInformation" />
<bean:define id="parameters" name="parameters" />

<fr:form action="<%= contextInformation.toString() + parameters.toString() %>">
	<html:hidden property="method" value="editExternalEnrolment"/>
	
	<bean:define id="studentId" name="student" property="idInternal" />
	<html:hidden property="studentId" value="<%= studentId.toString() %>"/>

	<fr:edit id="editExternalEnrolmentBean" 
			 name="externalEnrolmentBean"
			 schema="ExternalEnrolmentBean"
			 action="/externalUnits.do?method=editExternalEnrolment">
			 
		<fr:layout name="tabular-editable">
			<fr:property name="classes" value="tstyle4 thlight thright"/>
			<fr:property name="columnClasses" value=",,tdclear tderror1"/>
		</fr:layout>
	</fr:edit>
	<html:submit><bean:message key="button.submit" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:submit>
	<html:cancel onclick="this.form.method.value='backToMainPage';" ><bean:message key="button.cancel" bundle="ACADEMIC_OFFICE_RESOURCES"/></html:cancel>

</fr:form>
</logic:present>
