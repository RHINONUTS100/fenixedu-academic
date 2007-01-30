<%@ page language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<html:xhtml/>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@page import="net.sourceforge.fenixedu.presentationTier.servlets.filters.pathProcessors.ExecutionCourseProcessor"%>
<%@page import="org.apache.struts.util.RequestUtils"%>

<logic:present role="RESEARCHER">		

		<h2 id='pageTitle'> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.viewCurriculum.title"/> </h2>
	
		<bean:define id="personId" name="person" property="idInternal"/>
		
        <fr:form action="<%="/viewCurriculum.do?personOID=" + personId %>">
		<table class="tstyle5">
		<tr>
		<td>
		<bean:message key="label.common.chooseExecutionYear" bundle="DEPARTMENT_MEMBER_RESOURCES"/>: 
		</td>
		<td>
		<fr:edit id="executionYear" name="executionYearBean" slot="executionYear"> 
			<fr:layout name="menu-select-postback">
				<fr:property name="providerClass" value="net.sourceforge.fenixedu.presentationTier.renderers.providers.ExecutionYearsProvider"/>
				<fr:property name="format" value="${year}"/>
				<fr:property name="defaultText" value="label.all"/>
				<fr:property name="key" value="true"/>
				<fr:property name="bundle" value="RESEARCHER_RESOURCES"/>
			</fr:layout>
		</fr:edit>	
		</td>
		<td class="switchNone">
			<html:submit><bean:message key="button.submit" bundle="RESEARCHER_RESOURCES"/></html:submit>
		</td>
		</tr>
		</table>
		</fr:form>


		<p id="index" class="mbottom025"><strong><bean:message key="label.index" /></strong>:</p>
		<ol class="mtop025">
			<li><a href="#personalInformationTitle"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.viewCurriculum.personalInformationTitle"/></a></li>
		    <logic:notEmpty name="lectures">
		    <li><a href="#lecturesTitle"><bean:message bundle="DEPARTMENT_MEMBER_RESOURCES" key="label.teacher.details.lecturedCoursesInformation"/></a></li>
			</logic:notEmpty>
			<logic:notEmpty  name="final_works">	
				<li><a href="#guidancesTitle"><bean:message bundle="DEPARTMENT_MEMBER_RESOURCES" key="label.teacher.details.orientationInformation"/></a></li>
			</logic:notEmpty>
			<logic:empty  name="final_works">	
		    	<logic:notEmpty  name="guidances">	
					<li><a href="#guidancesTitle"><bean:message bundle="DEPARTMENT_MEMBER_RESOURCES" key="label.teacher.details.orientationInformation"/></a></li>
				</logic:notEmpty>
			</logic:empty>
			<logic:notEmpty name="functions">
			<li><a href="#functionsTitle"><bean:message bundle="DEPARTMENT_MEMBER_RESOURCES" key="label.teacher.details.functionsInformation"/></a></li>
			</logic:notEmpty>
			<logic:notEmpty name="researchInterests">
			<li><a href="#researchInterestsTitle"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.viewCurriculum.researchInterestsTitle"/></a></li>
			</logic:notEmpty>
			<logic:notEmpty name="resultPublications">
			<li><a href="#publicationsTitle"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.viewCurriculum.publicationsTitle"/></a></li>
			</logic:notEmpty>
			<logic:notEmpty name="resultPatents">
			<li><a href="#patentsTitle"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.viewCurriculum.patentsTitle"/></a></li>
			</logic:notEmpty>
		</ol>

	<!-- Personal Information -->
    <p id='personalInformationTitle' class="separator2" style="float: left; display: inline;"> 	<span class="fleft"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.viewCurriculum.personalInformationTitle"/></span> <span class="fright"><a href="#header"><bean:message key="label.top" /></a></span> </p>
	<div class="cboth"></div>
	<ul>			
	<li><bean:message key="researcher.viewCurriculum.name" bundle="RESEARCHER_RESOURCES"/>: <strong><fr:view name="person" property="nickname"/></strong> (<fr:view name="person" property="teacher.teacherNumber"/>)</li>
	<logic:present name="person" property="teacher">
		<li><bean:message key="label.teacher.category" bundle="APPLICATION_RESOURCES"/>: <fr:view name="person" property="teacher.category.name"/></li>
	</logic:present>
	</ul>
	
	<logic:present name="person" property="teacher">

	<!-- Lectures -->
	<logic:notEmpty name="lectures">
    <p id='lecturesTitle' class="separator2" style="float: left; display: inline;"> <span class="fleft"><bean:message bundle="DEPARTMENT_MEMBER_RESOURCES" key="label.teacher.details.lecturedCoursesInformation"/></span> <span class="fright"><a href="#header"><bean:message key="label.top" /></a></span> </p>
	<div class="cboth"></div>
		<ul>
  	    <logic:iterate id="lecture" name="lectures" type="net.sourceforge.fenixedu.domain.ExecutionCourse">
			<li>
			<a href="<%=RequestUtils.absoluteURL(request, ExecutionCourseProcessor.getExecutionCourseAbsolutePath(lecture))%>" target="_blank">
			<fr:view name="lecture" property="nome"/></a> (<fr:view name="lecture" property="executionYear.year"/>, <fr:view name="lecture" property="executionPeriod.name"/>, <fr:view name="lecture" property="degreePresentationString"/>)</li>
		</logic:iterate>
		</ul>
	</logic:notEmpty>

   	<!-- Final Works -->
    <logic:notEmpty  name="final_works">	
    <p id='guidancesTitle' class="separator2" style="float: left; display: inline;"> <span class="fleft"><bean:message bundle="DEPARTMENT_MEMBER_RESOURCES" key="label.teacher.details.orientationInformation"/></span> <span class="fright"><a href="#header"><bean:message key="label.top" /></a></span> </p>
	<div class="cboth"></div>

	</logic:notEmpty>
	<logic:empty  name="final_works">	
	    <logic:notEmpty  name="guidances">	
		    <p id='guidancesTitle' class="separator2" style="float: left; display: inline;"> <span class="fleft"><bean:message bundle="DEPARTMENT_MEMBER_RESOURCES" key="label.teacher.details.orientationInformation"/></span> <span class="fright"><a href="#header"><bean:message key="label.top" /></a></span> </p>
			<div class="cboth"></div>
		</logic:notEmpty>
	</logic:empty>
	
	<logic:notEmpty  name="final_works">	
		<p class="indent1"><em><bean:message key="label.common.finalDegreeWorks" bundle="DEPARTMENT_MEMBER_RESOURCES"/></em></p>
	
		<logic:iterate id="final_work" name="final_works">
			<ul style="float: left; width: 350px; margin: 0;">
			<li>
				<span class="color888"><fr:view name="final_work" property="startExecutionPeriod.executionYear.year"/></span> - 
				<fr:view name="final_work" property="student.person.name"/>(<fr:view name="final_work" property="student.number"/>)
			</li>
			</ul>
		</logic:iterate>
    </logic:notEmpty>
	
	<div class="cboth"></div>

	<logic:notEmpty name="guidances">
	<p class="indent1"><em><bean:message key="label.common.masterDegree" bundle="DEPARTMENT_MEMBER_RESOURCES"/></em></p>
	<ul>
	<logic:iterate id="guidance" name="guidances">
		<li><fr:view name="guidance" property="dissertationTitle"/>, <fr:view name="guidance" property="masterDegreeThesis.studentCurricularPlan.student.person.nome"/> (<bean:message key="label.teacher.details.orientationInformation.masterDegreeProofDate" bundle="DEPARTMENT_MEMBER_RESOURCES"/>: <fr:view name="guidance" property="masterDegreeThesis.activeMasterDegreeProofVersion.proofDate"/>)</li>
	</logic:iterate>
	</ul>	
	</logic:notEmpty>

	<div class="cboth"></div>

	<!-- Functions -->	
	<logic:notEmpty name="functions">
    <p id='functionsTitle' class="separator2" style="float: left; display: inline;"> <span class="fleft"><bean:message bundle="DEPARTMENT_MEMBER_RESOURCES" key="label.teacher.details.functionsInformation"/></span> <span class="fright"><a href="#header"><bean:message key="label.top" /></a></span> </p>
	<div class="cboth"></div>
	<ul>
	<logic:iterate id="personFunction" name="functions">
		<li>
			<span class="color888"><fr:view name="personFunction" property="beginDateInDateType"/> <bean:message key="label.until" bundle="RESEARCHER_RESOURCES"/> <fr:view name="personFunction" property="endDateInDateType"/></span>, 
			<fr:view name="personFunction" property="function.name"/> (<fr:view name="personFunction" property="unit.name"/>)
		</li>
	</logic:iterate>
	</ul>
	</logic:notEmpty>
	
	</logic:present>
			
	<!-- Research Interests -->
		<logic:notEmpty name="researchInterests">
	    <p id='researchInterestsTitle' class="separator2" style="float: left; display: inline;"> <span class="fleft"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.viewCurriculum.researchInterestsTitle"/></span> <span class="fright"><a href="#header"><bean:message key="label.top" /></a></span> </p>
		<div class="cboth"></div>

		<fr:view name="researchInterests" >
			<fr:layout>
				<fr:property name="ordered" value="true"/>
				<fr:property name="sortBy" value="order"/>
				<fr:property name="eachLayout" value="values"/>
				<fr:property name="eachSchema" value="researchInterest.title"/>
			</fr:layout>
		</fr:view>
		</logic:notEmpty>
		
		<%-- 
		<!-- Event Participation -->
		<h3 id='eventParticipationTitle' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.viewCurriculum.eventParticipationTitle"/> </span> </h3>
		<fr:view name="events">
			<fr:layout>
				<fr:property name="eachLayout" value="values-dash"/>
				<fr:property name="eachSchema" value="event.summary"/>
			</fr:layout>
		</fr:view>
		
		<!-- Project Participation -->
		<h3 id='projectParticipationTitle' class='cd_heading'/> <span> <bean:message bundle="RESEARCHER_RESOURCES" key="researcher.viewCurriculum.projectParticipationTitle"/> </span> </h3>
		
		<fr:view name="projects">
			<fr:layout>
				<fr:property name="eachLayout" value="values-dash"/>
				<fr:property name="eachSchema" value="project.summary"/>
			</fr:layout>
		</fr:view>
		--%>
		
		<!-- Publications -->
		<logic:notEmpty name="resultPublications">
	    <p id='publicationsTitle' class="separator2" style="float: left; display: inline;"> <span class="fleft"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.viewCurriculum.publicationsTitle"/></span> <span class="fright"><a href="#header"><bean:message key="label.top" /></a></span> </p>
		<div class="cboth"></div>
			<ul>
			<logic:iterate id="result" name="resultPublications" type="net.sourceforge.fenixedu.domain.research.result.publication.ResearchResultPublication">

			<li><fr:view name="result" layout="nonNullValues" schema="<%= result.getSchema() %>">
				<fr:layout>
					<fr:property name="classes" value="mbottom025"/>
					<fr:property name="htmlSeparator" value=", "/>
					<fr:property name="indentation" value="false"/>
				</fr:layout>
				<fr:destination name="view.publication" module="" path="<%="/researcher/resultPublications/showResultForOthers.do?resultId=" + result.getIdInternal() %>"/>
			</fr:view>
			</li>
			</logic:iterate>	
			</ul>
			</logic:notEmpty>
			<!--  
			<logic:empty name="resultPublications">
				<p><em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResultPublication.emptyList"/></em></p>
			</logic:empty>
			-->			
	
		
		<!-- Patents -->
		<logic:notEmpty name="resultPatents">
	    <p id='patentsTitle' class="separator2" style="float: left; display: inline;"> <span class="fleft"><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.viewCurriculum.patentsTitle"/></span> <span class="fright"><a href="#header"><bean:message key="label.top" /></a></span> </p>
		<div class="cboth"></div>
			<ul>
			<logic:iterate id="result" name="resultPatents" type="net.sourceforge.fenixedu.domain.research.result.patent.ResearchResultPatent">
				<li><strong>
 				<a href="<%= request.getContextPath() + "/researcher/resultPublications/showResultForOthers.do?resultId=" + result.getIdInternal() %>">
	 			<fr:view name="result" property="title"/></a>	 			
				</strong>
				<span style="color: #888;">
		 			<bean:message key="label.registrationDate" bundle="RESEARCHER_RESOURCES"/>
		 			<fr:view name="result" property="registrationDate"/>
		 		</span>
		 		 - 
	 			<span style="color: #888;">
	 				<bean:message key="label.approvalDate" bundle="RESEARCHER_RESOURCES"/>
		 			<fr:view name="result" property="approvalDate"/>
		 		</span></li>
			</logic:iterate>
			</ul>
			</logic:notEmpty>
			<!--  
			<logic:empty name="resultPatents">
				<p><em><bean:message bundle="RESEARCHER_RESOURCES" key="researcher.ResearchResultPatent.emptyList"/></em></p>
			</logic:empty>
			-->

</logic:present>

<script type="text/javascript" language="javascript">
switchGlobal();
</script>
