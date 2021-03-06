<%@ include file="/WEB-INF/view/common/taglibs.jsp" %>

<spring:eval expression="T(pl.edu.icm.saos.persistence.model.Judgment.JudgmentType).values()" var="enumJudgmentType" scope="page"/>


<c:if test="${showFilterBox == 'false'}" >
	<c:set var="showFilterBox" value="display-none" />
</c:if>

<div class="filter-box ${showFilterBox}" id="filter-box">
	<p>
		<spring:message code="judgmentSearch.filterBox.header" />
		<span id="filter-hide" class="filter-hide" data-toggle="tooltip" data-placement="right" title="<spring:message code='judgmentSearch.filterBox.hide' />"></span>
	</p>
	
	<saos:filterField assignedField="input-search-all" label="judgmentSearch.formField.all" filterValue="${judgmentCriteriaForm.all}" id="filter-all"></saos:filterField>
	
	<saos:filterField assignedField="input-search-casenumber" label="judgmentSearch.formField.caseNumber" filterValue="${judgmentCriteriaForm.signature}" id="filter-casenumber"></saos:filterField>
	
	<%-- Judgment date --%>
	<c:if test="${!empty judgmentCriteriaForm.dateFrom && judgmentCriteriaForm.dateFrom != ''}" >
		<p><spring:message code="judgmentSearch.formField.dateFrom" />:</p>
		<div class="filter-item" id="filter-date-from" data-assigned-field="datepicker_from">
			<div data-tooltip-text="<spring:message code='judgmentSearch.filterBox.removeFilter' />" >
				<c:out value="${judgmentCriteriaForm.dateFrom}" />
			</div>
		</div>
	</c:if>
	
	<c:if test="${!empty judgmentCriteriaForm.dateTo && judgmentCriteriaForm.dateTo != ''}" >
		<p><spring:message code="judgmentSearch.formField.dateTo" />:</p>
		<div class="filter-item" id="filter-date-to" data-assigned-field="datepicker_to">
			<div data-tooltip-text="<spring:message code='judgmentSearch.filterBox.removeFilter' />" >
				<c:out value="${judgmentCriteriaForm.dateTo}" />
			</div>
		</div>
	</c:if>

	
	<%-- Judgment type --%> 
	<c:if test="${!empty judgmentCriteriaForm.judgmentTypes}">
		<p><spring:message code="judgmentSearch.formField.judgmentType" />:</p>
		<c:forEach items="${judgmentCriteriaForm.judgmentTypes}" var="judgmentType" >
			<div class="filter-item" id="filter-judgment-type" data-assigned-field="checkbox-${fn:toLowerCase(judgmentType)}">
				<div data-tooltip-text="<spring:message code='judgmentSearch.filterBox.removeFilter' />">
					<saos:enum value="${judgmentType}" />
				</div>
			</div>
		</c:forEach>
	</c:if>
	
	
	<saos:filterField assignedField="input-search-judge" label="judgmentSearch.formField.judge" filterValue="${judgmentCriteriaForm.judgeName}" id="filter-judge"></saos:filterField>
	
	<saos:filterField assignedField="input-search-legalbases" label="judgmentSearch.formField.legalBases" filterValue="${judgmentCriteriaForm.legalBase}" id="filter-legal-base"></saos:filterField>
	
	<saos:filterField assignedField="input-search-referencedregulations" label="judgmentSearch.formField.referencedRegulations" filterValue="${judgmentCriteriaForm.referencedRegulation}" id="filter-referenced-regulations"></saos:filterField>
	
	<%--Court type --%>
	<c:if test="${!empty judgmentCriteriaForm.courtType}">
		<p><spring:message code="judgmentSearch.formField.courtType" />:</p>
		<div class="filter-item" id="filter-court-type" data-assigned-field="radio-court-${fn:toLowerCase(judgmentCriteriaForm.courtType)}">
			<div data-tooltip-text="<spring:message code='judgmentSearch.filterBox.removeFilter' />"><saos:enum value="${judgmentCriteriaForm.courtType}" /></div>
		</div>
	</c:if>
	
	<%@ include file="ccJudgment/ccJudgmentFilterFields.jsp" %>
	
	<%@ include file="scJudgment/scJudgmentFilterFields.jsp" %>
	
	<p id="clearAllFilters" class="clear-button" ><spring:message code="judgmentSearch.filterBox.clearAll" /></p>
		
	<p id="no-filters" >
		<spring:message code="judgmentSearch.filterBox.noFilters" />
	</p>
</div>

