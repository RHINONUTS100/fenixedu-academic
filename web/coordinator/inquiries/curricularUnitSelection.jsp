<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/fenix-renderers.tld" prefix="fr"%>
<%@ taglib uri="/WEB-INF/app.tld" prefix="app" %>
<html:xhtml />

<style>

div.progress-container {
  border: 1px solid #ccc; 
  width: 100px; 
  margin: 2px 5px 2px 0; 
  padding: 1px; 
  float: left; 
  background: white;
}

div.progress-container > div {
  background-color: #ACE97C; 
  height: 12px
}

</style>

<h2><bean:message key="title.inquiries.resultsWithDescription" bundle="INQUIRIES_RESOURCES"/></h2>

<p>
	<bean:message key="message.inquiries.coordinator.instructions" bundle="INQUIRIES_RESOURCES"/>
</p>

<html:form action="/viewInquiriesResults.do">
	<html:hidden property="method" value="prepare"/>
	<html:hidden property="degreeCurricularPlanID"/>
	<html:hidden property="executionDegreeID"/>
	<table class="tstyle5 thlight thright mvert05">
	<tr>
		<th>
		<label for="executionSemesterID"><bean:message key="label.inquiries.semesterAndYear" bundle="INQUIRIES_RESOURCES"/>:</label>
		</th>
		<td>
		<html:select property="executionSemesterID" onchange="this.form.method.value='selectexecutionSemester';this.form.submit();">
			<html:option value=""><bean:message key="label.inquiries.chooseAnOption" bundle="INQUIRIES_RESOURCES"/></html:option>
	 		<html:options collection="executionPeriods" property="idInternal" labelProperty="qualifiedName"/>
		</html:select>
		</td>
	</tr>
	</table>
</html:form>

<c:if test="${(not empty executionDegreeCoursesReport) and not (empty executionDegreeCoursesReport.executionInterval.coordinatorReportResponsePeriod)}">
    <div class="mtop1">
        <bean:write name="executionDegreeCoursesReport" property="executionInterval.coordinatorReportResponsePeriod.introduction" filter="false"/>
    </div>
</c:if>

<p class="separator2 mtop25"><b><bean:message key="title.coordinationReport.resultsToImprove" bundle="INQUIRIES_RESOURCES"/></b></p>
<logic:notEmpty name="executionCoursesToImproove">
<table>
    <th></th><th></th><th colspan="2"><bean:message key="label.inquiries.courseResults.teachingReports.filled" bundle="INQUIRIES_RESOURCES"/></th><th colspan="2">
    <logic:iterate id="studentInquiriesCourseResult" name="executionCoursesToImproove">
        <bean:define id="executionCourseID" name="studentInquiriesCourseResult" property="executionCourse.idInternal" />
        <bean:define id="executionDegreeID" name="executionDegreeID" />
        <tr>
            <td>
                <c:if test="${not empty studentInquiriesCourseResult.courseResultsCoordinatorComment}">
                    <html:img src="<%= request.getContextPath() + "/images/accept.gif"%>"  titleKey="label.inquiries.courseResults.coordinatorComments.filled" bundle="INQUIRIES_RESOURCES"/>
                </c:if>
            </td>
            <td class="width400px">
                <html:link page="<%= "/viewInquiriesResults.do?method=selectExecutionCourse&executionCourseID=" + executionCourseID  + "&executionDegreeID=" + executionDegreeID + "&amp;degreeCurricularPlanID=" + request.getAttribute("degreeCurricularPlanID") %>" >
                    <bean:write name="studentInquiriesCourseResult" property="executionCourse.nome"/>
                </html:link> 
                <c:if test="${studentInquiriesCourseResult.auditCU}"><span class="error0"><bean:message key="label.teachingInquiries.unsatisfactoryResultsAuditable" bundle="INQUIRIES_RESOURCES"/></span></c:if>
            </td>
            <td> 
                <fmt:formatNumber maxFractionDigits="0" value="${(studentInquiriesCourseResult.executionCourse.answeredTeachingInquiriesCount / studentInquiriesCourseResult.executionCourse.professorshipsCount) * 100}" var="ratio"/>
                <c:if test="${!studentInquiriesCourseResult.executionCourse.availableForInquiries}"><c:set var="ratio" value="100" /></c:if> 
                <div class="progress-container">          
                    <bean:define id="ratio" name="ratio" />
                    <div style="width: <%= ratio %>%"></div>
                </div>
            </td>
            <td>
                <bean:write name="studentInquiriesCourseResult" property="executionCourse.answeredTeachingInquiriesCount"/> / <bean:write name="studentInquiriesCourseResult" property="executionCourse.professorshipsCount"/>
            </td>
        </tr>
    </logic:iterate>
</table>
<p><img src="<%=request.getContextPath()%>/images/accept.gif"/> <em><bean:message key="label.inquiries.courseResults.coordinatorComments.filled" bundle="INQUIRIES_RESOURCES"/></em></p>
</logic:notEmpty>

<p class="separator2 mtop25"><b><bean:message key="title.coordinationReport.excellentResults" bundle="INQUIRIES_RESOURCES"/></b></p>
<logic:notEmpty name="excelentExecutionCourses">
<table>
    <th></th><th></th><th colspan="2"><bean:message key="label.inquiries.courseResults.teachingReports.filled" bundle="INQUIRIES_RESOURCES"/></th><th colspan="2">
    <logic:iterate id="studentInquiriesCourseResult" name="excelentExecutionCourses">
        <bean:define id="executionCourseID" name="studentInquiriesCourseResult" property="executionCourse.idInternal" />
        <bean:define id="executionDegreeID" name="executionDegreeID" />
        <tr>
            <td>
                <c:if test="${not empty studentInquiriesCourseResult.courseResultsCoordinatorComment}">
                    <html:img src="<%= request.getContextPath() + "/images/accept.gif"%>"  titleKey="label.inquiries.courseResults.coordinatorComments.filled" bundle="INQUIRIES_RESOURCES"/>
                </c:if>
            </td>
            <td class="width400px">
                <html:link page="<%= "/viewInquiriesResults.do?method=selectExecutionCourse&executionCourseID=" + executionCourseID  + "&executionDegreeID=" + executionDegreeID + "&amp;degreeCurricularPlanID=" + request.getAttribute("degreeCurricularPlanID") %>" >
                    <bean:write name="studentInquiriesCourseResult" property="executionCourse.nome"/>
                </html:link>
                <c:if test="${studentInquiriesCourseResult.auditCU}"><span class="error0"><bean:message key="label.teachingInquiries.unsatisfactoryResultsAuditable" bundle="INQUIRIES_RESOURCES"/></span></c:if>
            </td>
            <td> 
                <fmt:formatNumber maxFractionDigits="0" value="${(studentInquiriesCourseResult.executionCourse.answeredTeachingInquiriesCount / studentInquiriesCourseResult.executionCourse.professorshipsCount) * 100}" var="ratio"/>
                <c:if test="${!studentInquiriesCourseResult.executionCourse.availableForInquiries}"><c:set var="ratio" value="100" /></c:if> 
                <div class="progress-container">          
                    <bean:define id="ratio" name="ratio" />
                    <div style="width: <%= ratio %>%"></div>
                </div>
            </td>
            <td>
                <bean:write name="studentInquiriesCourseResult" property="executionCourse.answeredTeachingInquiriesCount"/> / <bean:write name="studentInquiriesCourseResult" property="executionCourse.professorshipsCount"/>
            </td>
        </tr>
    </logic:iterate>
</table>
<p><img src="<%=request.getContextPath()%>/images/accept.gif"/> <em><bean:message key="label.inquiries.courseResults.coordinatorComments.filled" bundle="INQUIRIES_RESOURCES"/></em></p>
</logic:notEmpty>

<p class="separator2 mtop25"><b><bean:message key="title.coordinationReport.otherResults" bundle="INQUIRIES_RESOURCES"/></b></p>
<logic:notEmpty name="otherExecutionCourses">
<table>
    <th></th><th></th><th colspan="2"><bean:message key="label.inquiries.courseResults.teachingReports.filled" bundle="INQUIRIES_RESOURCES"/></th><th colspan="2">
    <logic:iterate id="studentInquiriesCourseResult" name="otherExecutionCourses">
        <bean:define id="executionCourseID" name="studentInquiriesCourseResult" property="executionCourse.idInternal" />
        <bean:define id="executionDegreeID" name="executionDegreeID" />
        <tr>
            <td>
                <c:if test="${not empty studentInquiriesCourseResult.courseResultsCoordinatorComment}">
                    <html:img src="<%= request.getContextPath() + "/images/accept.gif"%>"  titleKey="label.inquiries.courseResults.coordinatorComments.filled" bundle="INQUIRIES_RESOURCES"/>
                </c:if>
            </td>
            <td class="width400px">
                <html:link page="<%= "/viewInquiriesResults.do?method=selectExecutionCourse&executionCourseID=" + executionCourseID  + "&executionDegreeID=" + executionDegreeID + "&amp;degreeCurricularPlanID=" + request.getAttribute("degreeCurricularPlanID") %>" >
                    <bean:write name="studentInquiriesCourseResult" property="executionCourse.nome"/>
                </html:link>
                <c:if test="${studentInquiriesCourseResult.auditCU}"><span class="error0"><bean:message key="label.teachingInquiries.unsatisfactoryResultsAuditable" bundle="INQUIRIES_RESOURCES"/></span></c:if>
            </td>
            <td> 
                <fmt:formatNumber maxFractionDigits="0" value="${(studentInquiriesCourseResult.executionCourse.answeredTeachingInquiriesCount / studentInquiriesCourseResult.executionCourse.professorshipsCount) * 100}" var="ratio"/> 
                <c:if test="${!studentInquiriesCourseResult.executionCourse.availableForInquiries}"><c:set var="ratio" value="100" /></c:if>
                <div class="progress-container">
                    <bean:define id="ratio" name="ratio" />
                    <div style="width: <%= ratio %>%"></div>
                </div>
            </td>
            <td>
                <bean:write name="studentInquiriesCourseResult" property="executionCourse.answeredTeachingInquiriesCount"/> / <bean:write name="studentInquiriesCourseResult" property="executionCourse.professorshipsCount"/>
            </td>
        </tr>
    </logic:iterate>
</table>
<p><img src="<%=request.getContextPath()%>/images/accept.gif"/> <em><bean:message key="label.inquiries.courseResults.coordinatorComments.filled" bundle="INQUIRIES_RESOURCES"/></em></p>
</logic:notEmpty>


<p class="separator2 mtop25"><b><bean:message key="title.coordinatorExecutionDegreeCoursesReport" bundle="INQUIRIES_RESOURCES"/></b></p>

<logic:equal name="canComment" value="true">

    <logic:empty name="courseResultsCoordinatorCommentEdit">
        <logic:notEmpty name="executionDegreeCoursesReport">
            <fr:view name="executionDegreeCoursesReport" schema="executionDegree.coordinatorExecutionDegreeCoursesReport" >
                <fr:layout name="tabular">
                    <fr:property name="classes" value="tstyle2 thlight thleft thtop width300px"/>
               </fr:layout>
            </fr:view>
        </logic:notEmpty>
        <html:form action="/viewInquiriesResults.do?courseResultsCoordinatorCommentEdit=true">
            <html:hidden property="method" value="selectexecutionSemester"/>
            <html:hidden property="degreeCurricularPlanID"/>
            <html:hidden property="executionDegreeID"/>
            <html:hidden property="executionSemesterID"/>
            <html:submit><bean:message key="label.inquiries.coordinatorExecutionDegreeCoursesReport.insert" bundle="INQUIRIES_RESOURCES"/></html:submit>
        </html:form>
    </logic:empty>
    <logic:notEmpty name="courseResultsCoordinatorCommentEdit">
        <html:form action="/viewInquiriesResults.do">
            <html:hidden property="method" value="selectexecutionSemester"/>
            <html:hidden property="degreeCurricularPlanID"/>
            <html:hidden property="executionDegreeID"/>
            <html:hidden property="executionSemesterID"/>
            <fr:edit name="executionDegreeCoursesReport" schema="executionDegree.coordinatorExecutionDegreeCoursesReport" >
                <fr:layout name="tabular-editable">
                    <fr:property name="classes" value="tstyle2 thlight thleft thtop"/>
                    <fr:property name="columnClasses" value="width300px,,"/>
               </fr:layout>
            </fr:edit>
            <html:submit><bean:message key="label.submit" bundle="APPLICATION_RESOURCES"/></html:submit>
        </html:form>
    </logic:notEmpty>

</logic:equal>

<logic:equal name="canComment" value="false">
    <logic:notEmpty name="executionDegreeCoursesReport">
        <fr:view name="executionDegreeCoursesReport" schema="executionDegree.coordinatorExecutionDegreeCoursesReport" >
            <fr:layout name="tabular">
                <fr:property name="classes" value="tstyle2 thlight thleft thtop width300px"/>
           </fr:layout>
        </fr:view>
    </logic:notEmpty>
</logic:equal>
