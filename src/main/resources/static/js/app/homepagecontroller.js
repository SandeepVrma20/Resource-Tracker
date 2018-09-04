'use strict';
/**
 * Controller for the home page.
 * History:
 * 
 * Developer         Date       Change Reason     Change
 * ----------------- ---------- ----------------- ----------------
 * Masud Hasan       04-feb-15  Initial version   Release 1.0
 * 
 */
provisionAdjustmentControllers
      .controller(
            'HomePageController',
            [
                '$window',
                '$scope',
                '$rootScope',
                '$location',
                'ProvisionAdjustmentService',
                'pasCache',
                'pasCacheKeys',
                'genPasService',
                'gettextCatalog',
                '$cookieStore',
                'CalculationService',
                'ErrorService',
                function($window, $scope, $rootScope, $location, ProvisionAdjustmentService, pasCache, pasCacheKeys, genPasService, gettextCatalog,
                      $cookieStore, CalculationService, ErrorService) {
                  $scope.displayRadioButtonFlag = true;
                  displayRadioButton();
                  //
                  $scope.currentLanguage = 'en';
                  $scope.homePageFormData = {};
                  $scope.homePageFormData.ifrs = {};
                  $scope.disableProceedButton = true;
                  $scope.home = {};
                  $scope.home.showField = true;
                  $scope.home.showLeftArrow = false; // for hiding
                  $scope.home.showRightArrow = false; // for unhiding
                  $scope.showIFRS9 = false; // for hiding the IFRS9 workflows
                  $scope.home.showTable = true;
                  $scope.home.viewWorkflow = false;
                  $scope.mdldata = [];
                  $scope.mdldata.list = [];
                  $scope.formData = {};
                  $scope.input = {};
                  $scope.isDisabled = false;
                  $scope.formData.isVersionPresent = false;
                  var keyUserAction = pasCacheKeys.pck_useractions;
                  var backActionKey = pasCacheKeys.pck_backActionFrom;
                  $("[rel=popover]").popover('destroy');
                  $scope.fmuser = false;
                  $scope.isValidationFailed = 1;
                  pasCache.put('userid', $scope.userId);
                  var backFromProfileOverview = false;
                  var backFromProfileOverviewCalcRequestId;
                  var reportingPeriodCalcReqcount = 0;
                  $cookieStore.remove('tabSelected');
                  $cookieStore.remove('checked');
                  // Nilesh Changes Begin
                  $scope.homepageTabSelected = true;
                  $scope.manageWorkflowsTab = false;
                  var calcRequestMap = {};
                  $scope.disableSkipAdjButton = true;
                  $scope.disablePoApproveButton = true;
                  var skipAdjList = [];
                  var poApproveList = [];
                  $scope.input.pocccApprovalReason = "";
                  $scope.input.calcRequestString = "";
                  // Nilesh Changes End
                  if (pasCache.get('page_reload_manage_workflow_tab') != undefined && pasCache.get('page_reload_manage_workflow_tab') == true) {
                    $scope.homepageTabSelected = false;
                    $scope.manageWorkflowsTab = true;
                    $scope.showIFRS9 = true;
                  }

                  if (pasCache.get('session_back_button_click') != undefined) {
                    backFromProfileOverview = pasCache.get('session_back_button_click').session_back_button_click;
                    if (backFromProfileOverview) {
                      var showIFRS9_local = pasCache.get('back_to_ifrs');
                      backFromProfileOverviewCalcRequestId = pasCache.get(pasCacheKeys.pck_calcRequestId);
                      if (showIFRS9_local != undefined) {
                        $scope.showIFRS9 = pasCache.get('back_to_ifrs');
                      }
                    }
                    pasCache.remove();
                  }
                  getReleaseNotes();
                  getWorkflows();
                  getIfrsWorkflows();
                  showFunctionMaintanceLink();

                  // Nilesh Changes Begin
                  $scope.onclicktab = function(type) {
                    if (type == "homepageTab") {
                      $scope.homepageTabSelected = true;
                      $scope.manageWorkflowsTab = false;
                    } else if (type == "manageWorkflowsTab") {
                      $scope.homepageTabSelected = false;
                      $scope.manageWorkflowsTab = true;
                    }
                  }
                  // Nilesh Changes End

                  $scope.loadPortfolioOverview = function(calculationRequestId, calcReqType, workflow, reportingPeriod) {
                    pasCache.put(pasCacheKeys.pck_calcRequestId, calculationRequestId);
                    pasCache.put(pasCacheKeys.pck_workflows, workflow);
                    pasCache.put(pasCacheKeys.pck_reporting_period, reportingPeriod);
                    pasCache.put('back_to_ifrs', $scope.showIFRS9);
                    $location.path(
                          '/calculationRequestId/' + calculationRequestId + '/calcReqType/' + calcReqType + '/workflowtype/' + workflow.wfTypeId
                                + '/workflows/' + workflow.wfId).search({});

                  };

                  $scope.loadIfrsPortfolioOverview = function(calculationRequestId, calcReqType, calculationRequestStatus, sourceSystem) {

                    pasCache.put(pasCacheKeys.pck_calcRequestId, calculationRequestId);
                    pasCache.put('back_to_ifrs', $scope.showIFRS9);
                    pasCache.put(pasCacheKeys.pck_deliveryEntity, sourceSystem);
                    pasCache.put(pasCacheKeys.pck_calculationrequest_status, calculationRequestStatus);
                    $location.path('/calculationRequestId/' + calculationRequestId + '/calcReqType/' + calcReqType + '/delEntity/' + sourceSystem)
                          .search({});

                  };

                  //**********private function starts**********

                  function getMaxCubeRefreshCount() {
                    ProvisionAdjustmentService.getMaxCubeRefreshCount().success(function(data, status, headers, config) {
                      $scope.refreshDate = data;
                      var current = new Date();
                      var dd = current.getDate();
                      var mm = current.getMonth() + 1; //January is 0!

                      var yyyy = current.getFullYear();
                      if (dd < 10) {
                        dd = '0' + dd
                      }
                      if (mm < 10) {
                        mm = '0' + mm
                      }
                      var today = dd + '-' + mm + '-' + yyyy;
                      if (today == $scope.refreshDate) {
                        $scope.isDisabled = true;
                      }
                    });
                  }

                  function getReleaseNotes() {
                    ProvisionAdjustmentService.getApplicationInformation().success(function(data, status, headers, config) {
                      $scope.homePageFormData.releaseNotes = data.releaseNotes;
                    });
                  }

                  function displayRadioButton() {
                    var propertyName = gettextCatalog.getString("LABEL_PROPERTY_DISPLAY_IFRS_WORKFLOWS");
                    ProvisionAdjustmentService.getProperty(propertyName).success(function(data, status, headers, config) {
                      if (data.propertyValue != undefined && data.propertyValue == gettextCatalog.getString("LABEL_PROPERTY_VALUE_N")) {
                        $scope.displayRadioButtonFlag = false;
                      }
                    });
                  }

                  function showFunctionMaintanceLink() {
                    ProvisionAdjustmentService.getUserActions().success(function(useraccessdata) {
                      $scope.functionalMaintanenceLink = useraccessdata.functionalMaintenance;

                    });
                  }

                  function getWorkflows() {
                    ProvisionAdjustmentService
                          .getWorkFlows("Y", 0, 1)
                          .success(
                                function(data, status, headers, config) {
                                  $scope.preCheckedCalIds = [];
                                  $scope.homePageFormData.workflows = {};
                                  $scope.homePageFormData.workflows.wfTypeId = data.wfTypeId;
                                  $scope.homePageFormData.workflows.reportingPeriodDataList = {};
                                  $scope.homePageFormData.workflows.reportingPeriodDataList.reportingPeriodList = data.reportingPeriodListForHome;
                                  //$scope.latestCalculationReqId = $scope.homePageFormData.workflows.reportingPeriodDataList.reportingPeriodList[0].calculationRequestVOList[0].id;
                                  $scope.latestIdArray = [];
                                  var temp = 0;

                                  //calculate no of workflowtypes present per reporting period. To align properly on Homescreen
                                  angular
                                        .forEach(
                                              $scope.homePageFormData.workflows.reportingPeriodDataList.reportingPeriodList,
                                              function(reportingPeriodData) {
                                                //no of workflows present per reporting period. To align properly on Homescreen
                                                var wfTypesCount = 0;
                                                var propertySourceSystem = reportingPeriodData.propertySourceSystem;
                                                angular.forEach(reportingPeriodData.calculationRequestVOList, function(calculationRequestData) {
                                                  wfTypesCount += calculationRequestData.workflowList.length;
                                                  if (calculationRequestData.status == gettextCatalog.getString("LABEL_STATUS_REJECTED")
                                                        || calculationRequestData.status == gettextCatalog.getString("LABEL_STATUS_PART_CLEAN")
                                                        || calculationRequestData.status == gettextCatalog.getString("LABEL_STATUS_CLEAN")
                                                        || calculationRequestData.status == gettextCatalog.getString("LABEL_STATUS_EL_REJ_PRG")
                                                        || calculationRequestData.status == gettextCatalog.getString("LABEL_STATUS_PENDING")) {
                                                    calculationRequestData.viewWorkflow = false;
                                                  } else {
                                                    calculationRequestData.viewWorkflow = true;
                                                  }

                                                  calculationRequestData.backFromOverview = false;
                                                  if (backFromProfileOverviewCalcRequestId != undefined
                                                        && calculationRequestData.id == backFromProfileOverviewCalcRequestId) {
                                                    calculationRequestData.backFromOverview = true;
                                                  }

                                                  if (calculationRequestData != undefined && calculationRequestData.mstrCalcFilter == true) {
                                                    $scope.preCheckedCalIds.push(calculationRequestData.id);
                                                  }

                                                  $scope.latestIdArray.push(calculationRequestData.id);
                                                  var totalWf = 0;
                                                  var poApproveWf = 0;
                                                  angular.forEach(calculationRequestData.workflowList, function(workFlowData) {
                                                    if (workFlowData.wfStatus == genPasService.constants.STATUS_WORKFLOW_PO_APPROVED) {
                                                      poApproveWf++;
                                                    }
                                                    totalWf++;
                                                  });
                                                  if (totalWf > 0 && totalWf == poApproveWf
                                                        && propertySourceSystem == calculationRequestData.sourceSystem
                                                        && 1 == calculationRequestData.modelBasedCalcFlag) {
                                                    calculationRequestData.poApprovedWf = true;
                                                  } else {
                                                    calculationRequestData.poApprovedWf = false;
                                                  }

                                                });

                                                $scope.homePageFormData.workflows.reportingPeriodDataList.reportingPeriodList[temp++].wfTypesCount = wfTypesCount;

                                              });
                                  userType();

                                });
                  }

                  function getIfrsWorkflows() {
                    // Call for fetching IFRS9 workflows
                    ProvisionAdjustmentService
                          .getWorkFlows("Y", 0, 4)
                          .success(
                                function(data, status, headers, config) {
                                  $scope.preCheckedIfrsCalIds = [];
                                  $scope.homePageFormData.ifrs.workflows = {};
                                  $scope.homePageFormData.ifrs.workflows.wfTypeId = data.wfTypeId;
                                  $scope.homePageFormData.ifrs.workflows.reportingPeriodDataList = {};
                                  $scope.homePageFormData.ifrs.workflows.reportingPeriodDataList.reportingPeriodList = data.reportingPeriodListForHome;
                                  //$scope.latestCalculationReqId = $scope.homePageFormData.ifrs.workflows.reportingPeriodDataList.reportingPeriodList[0].calculationRequestVOList[0].id;

                                  // Nilesh Changes Begin
                                  $scope.homePageFormData.ifrs.manageworkflows = {};
                                  $scope.homePageFormData.ifrs.manageworkflows.presentMonthList = data.presentMonthList;
                                  $scope.homePageFormData.ifrs.manageworkflows.presentMonthList.uniqueStatusMap = {};
                                  prepareFilterNames();
                                  // Nilesh Changes End

                                  var wfTypesCount = 0;
                                  var temp = 0;
                                  $scope.latestIfrsIdArray = [];

                                  //calculate no of workflowtypes present per reporting period. To align properly on Homescreen
                                  angular
                                        .forEach(
                                              $scope.homePageFormData.ifrs.workflows.reportingPeriodDataList.reportingPeriodList,
                                              function(reportingPeriodData) {
                                                // var propertySourceSystem = reportingPeriodData.propertySourceSystem;
                                                var filterList = reportingPeriodData.workFlowStatusFilterList;
                                                setFilters(filterList);
                                                displayIfrsCalculationRequest(reportingPeriodData, wfTypesCount, temp);
                                                $scope.homePageFormData.ifrs.workflows.reportingPeriodDataList.reportingPeriodList[temp++].wfTypesCount = wfTypesCount;
                                                reportingPeriodData.isCalcReqAvailable = true;
                                                reportingPeriodData.calReqListLength = reportingPeriodData.calculationRequestVOList.length;
                                              });
                                  userType();

                                });
                  }

                  function prepareFilterNames() {
                    angular.forEach($scope.homePageFormData.ifrs.manageworkflows.presentMonthList, function(repPeriodData) {
                      angular.forEach(repPeriodData.calculationRequestVOList, function(calculationRequest) {
                        calculationRequest.display = true;
                        var status = calculationRequest.ifrsCalcReqWfStatus;
                        if (status != undefined) {
                          $scope.homePageFormData.ifrs.manageworkflows.presentMonthList.uniqueStatusMap[status] = false;
                        }
                      });
                    });
                    /*
                    for ( var status in statusMap) {
                      $scope.homePageFormData.ifrs.manageworkflows.presentMonthList.uniqueStatusList
                          .push({
                            'name' : status,
                            'selected' : false
                          });
                    }
                    $scope.homePageFormData.ifrs.manageworkflows.presentMonthList.uniqueStatusList
                        .sort();
                    */
                  }

                  function resetSelectionMap() {
                    for ( var status in $scope.homePageFormData.ifrs.manageworkflows.presentMonthList.uniqueStatusMap) {
                      $scope.homePageFormData.ifrs.manageworkflows.presentMonthList.uniqueStatusMap[status] = false;
                    }
                  }

                  $scope.filterRows = function(status) {
                    $scope.resetCheckboxes();
                    var displayAll = false;
                    if ($scope.homePageFormData.ifrs.manageworkflows.presentMonthList.uniqueStatusMap[status]) {
                      resetSelectionMap();
                      displayAll = true;
                    } else {
                      resetSelectionMap();
                      $scope.homePageFormData.ifrs.manageworkflows.presentMonthList.uniqueStatusMap[status] = true;
                    }
                    angular.forEach($scope.homePageFormData.ifrs.manageworkflows.presentMonthList, function(repPeriodData) {
                      angular.forEach(repPeriodData.calculationRequestVOList, function(calculationRequest) {
                        calculationRequest.display = false;
                        if (displayAll || calculationRequest.ifrsCalcReqWfStatus == status) {
                          calculationRequest.display = true;
                        }
                      });
                    });
                  };

                  //OnClick IFRS workFlow Filters
                  function onClickWfFilter(filter) {
                    var wfTypesCount = 0;
                    var temp = 0;
                    if ($scope.filter != "") {
                      angular.forEach($scope.homePageFormData.ifrs.workflows.reportingPeriodDataList.reportingPeriodList, function(
                            reportingPeriodData) {
                        reportingPeriodCalcReqcount = 0;
                        displayIfrsCalculationRequestForWfFilter(reportingPeriodData, wfTypesCount, temp, filter);
                        $scope.homePageFormData.ifrs.workflows.reportingPeriodDataList.reportingPeriodList[temp++].wfTypesCount = wfTypesCount;
                        if (reportingPeriodCalcReqcount > 0) {
                          reportingPeriodData.isCalcReqAvailable = true;
                          reportingPeriodData.calReqListLength = reportingPeriodCalcReqcount;
                        } else {
                          reportingPeriodData.isCalcReqAvailable = false;
                        }
                      });
                    } else if ($scope.filter == "") {
                      angular.forEach($scope.homePageFormData.ifrs.workflows.reportingPeriodDataList.reportingPeriodList, function(
                            reportingPeriodData) {
                        reportingPeriodCalcReqcount = 0;
                        displayIfrsCalculationRequest(reportingPeriodData, wfTypesCount, temp);
                        $scope.homePageFormData.ifrs.workflows.reportingPeriodDataList.reportingPeriodList[temp++].wfTypesCount = wfTypesCount;
                        reportingPeriodData.isCalcReqAvailable = true;
                        reportingPeriodData.calReqListLength = reportingPeriodData.calculationRequestVOList.length;
                      });
                    }
                  }

                  /** IFRS CalculationRequest display for workFlow Filter*/
                  function displayIfrsCalculationRequestForWfFilter(reportingPeriodData, wfTypesCount, temp, filter) {
                    var calcReqCount = 0;
                    angular
                          .forEach(
                                reportingPeriodData.calculationRequestVOList,
                                function(calculationRequestData) {
                                  wfTypesCount += calculationRequestData.workflowList.length;
                                  if (calculationRequestData.status == gettextCatalog.getString("LABEL_STATUS_REJECTED")
                                        || calculationRequestData.status == gettextCatalog.getString("LABEL_STATUS_PART_CLEAN")
                                        || calculationRequestData.status == gettextCatalog.getString("LABEL_STATUS_CLEAN")
                                        || calculationRequestData.status == gettextCatalog.getString("LABEL_STATUS_EL_REJ_PRG")
                                        || calculationRequestData.status == gettextCatalog.getString("LABEL_STATUS_PENDING")) {
                                    calculationRequestData.viewWorkflow = false;
                                  } else {
                                    calculationRequestData.viewWorkflow = true;
                                  }

                                  /**To check ifrsWorkflow status */
                                  if (filter == calculationRequestData.ifrsCalcReqWfStatus) {
                                    calculationRequestData.isWfFilterselected = true;
                                    reportingPeriodCalcReqcount++;
                                  } else {
                                    calculationRequestData.isWfFilterselected = false;
                                  }

                                  calculationRequestData.backFromOverview = false;
                                  if (backFromProfileOverviewCalcRequestId != undefined
                                        && calculationRequestData.id == backFromProfileOverviewCalcRequestId) {
                                    calculationRequestData.backFromOverview = true;
                                  }

                                  if (calculationRequestData != undefined && calculationRequestData.mstrCalcFilter == true) {
                                    $scope.preCheckedIfrsCalIds.push(calculationRequestData.id);
                                  }

                                  $scope.latestIfrsIdArray.push(calculationRequestData.id);
                                  var totalWf = 0;
                                  var poApproveWf = 0;

                                  angular.forEach(calculationRequestData.workflowList, function(workFlowData) {
                                    if (workFlowData.wfStatus == genPasService.constants.STATUS_WORKFLOW_PO_APPROVED) {
                                      poApproveWf++;
                                    }
                                    totalWf++;
                                  });
                                  if (totalWf > 0 && totalWf == poApproveWf && 1 == calculationRequestData.modelBasedCalcFlag) {
                                    calculationRequestData.poApprovedWf = true;
                                  } else {
                                    calculationRequestData.poApprovedWf = false;
                                  }
                                  if (calculationRequestData.workflowList.length > 0) {
                                    var workflows = null;
                                    workflows = calculationRequestData.workflowList[0];
                                    $scope.homePageFormData.ifrs.workflows.reportingPeriodDataList.reportingPeriodList[temp].calculationRequestVOList[calcReqCount].workflowList = {};
                                    $scope.homePageFormData.ifrs.workflows.reportingPeriodDataList.reportingPeriodList[temp].calculationRequestVOList[calcReqCount].workflowList[0] = workflows;
                                    calcReqCount++;
                                  }
                                });
                  }

                  /** IFRS CalculationRequest display */
                  function displayIfrsCalculationRequest(reportingPeriodData, wfTypesCount, temp) {
                    var calcReqCount = 0;
                    angular
                          .forEach(
                                reportingPeriodData.calculationRequestVOList,
                                function(calculationRequestData) {
                                  wfTypesCount += calculationRequestData.workflowList.length;
                                  if (calculationRequestData.status == gettextCatalog.getString("LABEL_STATUS_REJECTED")
                                        || calculationRequestData.status == gettextCatalog.getString("LABEL_STATUS_PART_CLEAN")
                                        || calculationRequestData.status == gettextCatalog.getString("LABEL_STATUS_CLEAN")
                                        || calculationRequestData.status == gettextCatalog.getString("LABEL_STATUS_EL_REJ_PRG")
                                        || calculationRequestData.status == gettextCatalog.getString("LABEL_STATUS_PENDING")) {
                                    calculationRequestData.viewWorkflow = false;
                                  } else {
                                    calculationRequestData.viewWorkflow = true;
                                  }

                                  calculationRequestData.isWfFilterselected = true;
                                  calculationRequestData.backFromOverview = false;
                                  if (backFromProfileOverviewCalcRequestId != undefined
                                        && calculationRequestData.id == backFromProfileOverviewCalcRequestId) {
                                    calculationRequestData.backFromOverview = true;
                                  }

                                  if (calculationRequestData != undefined && calculationRequestData.mstrCalcFilter == true) {
                                    $scope.preCheckedIfrsCalIds.push(calculationRequestData.id);
                                  }

                                  $scope.latestIfrsIdArray.push(calculationRequestData.id);
                                  var totalWf = 0;
                                  var poApproveWf = 0;

                                  angular.forEach(calculationRequestData.workflowList, function(workFlowData) {
                                    if (workFlowData.wfStatus == genPasService.constants.STATUS_WORKFLOW_PO_APPROVED) {
                                      poApproveWf++;
                                    }
                                    totalWf++;
                                  });
                                  if (totalWf > 0 && totalWf == poApproveWf && 1 == calculationRequestData.modelBasedCalcFlag) {
                                    calculationRequestData.poApprovedWf = true;
                                  } else {
                                    calculationRequestData.poApprovedWf = false;
                                  }
                                  if (calculationRequestData.workflowList.length > 0) {
                                    var workflows = null;
                                    workflows = calculationRequestData.workflowList[0];
                                    $scope.homePageFormData.ifrs.workflows.reportingPeriodDataList.reportingPeriodList[temp].calculationRequestVOList[calcReqCount].workflowList = {};
                                    $scope.homePageFormData.ifrs.workflows.reportingPeriodDataList.reportingPeriodList[temp].calculationRequestVOList[calcReqCount].workflowList[0] = workflows;
                                    calcReqCount++;
                                  }
                                });
                  }

                  //Set IFRS workFlow Filters
                  function setFilters(filterNames) {
                    var listWFFilter = [];
                    angular.forEach(filterNames, function(filterName) {
                      if (filterName == $scope.filter) {
                        listWFFilter.push({
                          "name": filterName,
                          "selected": true
                        });
                      } else {
                        listWFFilter.push({
                          "name": filterName,
                          "selected": false
                        });
                      }
                    });
                    $scope.homePageFormData.ifrs.workflows.reportingPeriodDataList.workFlowStatusFilterList = listWFFilter;
                  }

                  //Workflow filter on Home screen
                  $scope.changeFilterBg = function(item) {
                    var wfFilter = $scope.homePageFormData.ifrs.workflows.reportingPeriodDataList.workFlowStatusFilterList;
                    angular.forEach(wfFilter, function(wfStatus) {
                      if (wfStatus.name != item.name) {
                        wfStatus.selected = false;
                      } else {
                        wfStatus.selected = !wfStatus.selected;
                      }
                    });
                    if (item.selected) {
                      $scope.filter = item.name;
                    } else {
                      $scope.filter = "";
                    }
                    //cache the selected filter
                    pasCache.put('wfFilter', $scope.filter);
                    onClickWfFilter($scope.filter);
                  };

                  function userType() {
                    ProvisionAdjustmentService.getUserActions().success(function(useraccessdata) {
                      pasCache.put(keyUserAction, useraccessdata);
                      setInitialButton(useraccessdata);
                    });
                  }

                  function setInitialButton(pasUserActions) {
                    $scope.fmuser = pasUserActions.functionalMaintenance;
                    if ($scope.fmuser) {
                      $scope.home.showField = true;
                      $scope.home.showLeftArrow = false; //for hiding
                      $scope.home.showRightArrow = false; //for unhiding
                    } else {
                      var element = angular.element('#homeTable');
                      element.removeClass('homeTab');
                      $scope.home.showTable = false;
                      //element.addClass('homeTabhidden');
                      $scope.home.showField = false;
                      $scope.home.showLeftArrow = false; //for hiding
                      $scope.home.showRightArrow = true; //for unhiding
                    }
                    $scope.poApprover = pasUserActions.poApprover;
                    getMaxCubeRefreshCount();
                  }

                  function setDefaults(wftdata) {
                    genPasService.getDefaultValues().success(function(defaults) {
                      var defWft = defaults.default_workflowtype;
                      if (defWft == undefined) {
                        defWft = '';
                      }
                      var present = false;
                      for (let index = 0; index < wftdata.length; index++) {
                        if (wftdata[index].id == defWft && wftdata[index].active) {
                          present = true;
                          break;
                        }
                      }
                      if (!present) {
                        for (let index = 0; index < wftdata.length; index++) {
                          if (wftdata[index].active) {
                            defWft = wftdata[index].id;
                            break;
                          }
                        }
                      }
                      $scope.homePageFormData.selectedWorkflowtype = defWft
                    }).error(function() {
                      for (let index = 0; index < wftdata.length; index++) {
                        if (wftdata[index].active) {
                          $scope.homePageFormData.selectedWorkflowtype = wftdata[index].id;
                          break;
                        }
                      }
                    });
                  }

                  $(document).ready(function() {
                    $scope.isButtonVisible = false;
                    $('#btn').tooltip('hide');
                    $(window).scroll(function() {
                      if ($(window).scrollTop() > 100) {
                        $scope.isButtonVisible = true;
                        $('#back-to-top').fadeIn();
                      } else {
                        $scope.isButtonVisible = false;
                        $('#back-to-top').fadeOut();
                      }
                    });
                    // scroll body to 0px on click
                    $('#back-to-top').click(function() {

                      $scope.isTopButtonVisible = true;
                      $('#top').tooltip('show');
                      $('#back-to-top').tooltip('hide');
                      $('body,html').animate({
                        scrollTop: 0
                      }, 800);
                      return false;
                    });

                    $('#back-to-top').tooltip('show');
                  });

                  $scope.updateCubeRefresh = function(calcType) {
                    var preCheckedCalIds = [];
                    var latestIdArray = [];
                    $scope.postCheckedCalIds = [];
                    $scope.newUnCheckedCalIds = [];
                    $scope.newCheckedCalIds = [];

                    if (calcType == 1) {
                      preCheckedCalIds = $scope.preCheckedCalIds;
                      latestIdArray = $scope.latestIdArray;
                      $('td[name="ias39CheckboxTd"] > input:checkbox').each(function() {
                        if ($(this).is(":checked")) {
                          $scope.postCheckedCalIds.push($(this).attr('id'));
                        }
                      });
                    } else if (calcType == 4) {
                      preCheckedCalIds = $scope.preCheckedIfrsCalIds;
                      latestIdArray = $scope.latestIfrsIdArray;
                      $('td[name="ifrsCheckboxTd"] > input:checkbox').each(function() {
                        if ($(this).is(":checked")) {
                          $scope.postCheckedCalIds.push($(this).attr('id'));
                        }
                      });
                    }

                    for (let i = 0; i < preCheckedCalIds.length; i++) {
                      let isValFound = true;
                      let value = preCheckedCalIds[i];
                      for (let j = 0; j < $scope.postCheckedCalIds.length; j++) {
                        if (value == $scope.postCheckedCalIds[j]) {
                          isValFound = false;
                          break;
                        }
                      }
                      if (isValFound == true) {
                        $scope.newUnCheckedCalIds.push(value);
                      }
                    }

                    for (let i = 0; i < $scope.postCheckedCalIds.length; i++) {
                      let isValFound = true;
                      let value = $scope.postCheckedCalIds[i];
                      for (let j = 0; j < preCheckedCalIds.length; j++) {
                        if (value == preCheckedCalIds[j]) {
                          isValFound = false;
                          break;
                        }
                      }
                      if (isValFound == true) {
                        $scope.newCheckedCalIds.push(value);
                      }
                    }

                    latestIdArray.sort(function(a, b) {
                      return b - a
                    });

                    for (var i = 0; i < latestIdArray.length; i++) {
                      $scope.latestCalcId = latestIdArray[i];
                      break;
                    }

                    var sysDate = new Date();
                    var dd = sysDate.getDate();
                    var mm = sysDate.getMonth() + 1; // January
                    // is 0!

                    var yyyy = sysDate.getFullYear();
                    if (dd < 10) {
                      dd = '0' + dd
                    }
                    if (mm < 10) {
                      mm = '0' + mm
                    }
                    var currentDate = dd + '-' + mm + '-' + yyyy;
                    var calRequestIdMap = {
                      'unCheckCalIdLists': $scope.newUnCheckedCalIds,
                      'checkCalIdLists': $scope.newCheckedCalIds
                    };

                    var validationOk = true;
                    if ($scope.newUnCheckedCalIds.length > 3) {
                      validationOk = false;
                      $scope.isValidationFailed = 0;
                      ErrorService.showError('LABEL_VALIDATION_DESELECT_CUBE_REFRESH');
                    } else if ($scope.newCheckedCalIds.length > 3) {
                      validationOk = false;
                      $scope.isValidationFailed = 0;
                      ErrorService.showError('LABEL_VALIDATION_SELECT_CUBE_REFRESH');
                    } else if ($scope.postCheckedCalIds.length > 15) {
                      validationOk = false;
                      $scope.isValidationFailed = 0;
                      ErrorService.showError('LABEL_VALIDATION_MAX_CHECKED_CUBE_REFRESH');
                    }

                    if (validationOk) {
                      ErrorService.removeAllErrors();
                      ProvisionAdjustmentService.updateCubeRefresh(calRequestIdMap, currentDate, $scope.latestCalcId, calcType).success(
                            function(data) {
                              $window.alert(data);
                              getWorkflows();
                            }).error(function(data, status) {
                        if (status == 400) {
                          $scope.isValidationFailed = 0;
                          ErrorService.showServerErrors(data.messages);
                        }
                      });
                    }

                  };

                  $scope.rejectCalculationRequest = function(calculationRequestObj, calcType) {
                    var id = calculationRequestObj.id; //alert(id);
                    var validationOk = false;
                    var continueLoop = true;

                    if (calculationRequestObj.status == 'REJECTED') {
                      return false;
                    }

                    if (calcType == 1) {
                      angular.forEach($scope.homePageFormData.workflows.reportingPeriodDataList.reportingPeriodList, function(reportingPeriodData) {
                        angular.forEach(reportingPeriodData.calculationRequestVOList, function(calculationRequestData) {
                          if (continueLoop) {
                            if (calculationRequestData.status == gettextCatalog.getString('LABEL_PENDING') && calculationRequestData.id == id) {
                              validationOk = true;
                              ErrorService.showError('LABEL_VALIDATION_PENDING_CALCULATION_REQUEST_REJECT');
                              continueLoop = false;
                            }
                          }
                        });

                      });
                    } else if (calcType == 4) {
                      angular.forEach($scope.homePageFormData.ifrs.workflows.reportingPeriodDataList.reportingPeriodList, function(
                            reportingPeriodData) {
                        angular.forEach(reportingPeriodData.calculationRequestVOList, function(calculationRequestData) {
                          if (continueLoop) {
                            if (calculationRequestData.status == gettextCatalog.getString('LABEL_PENDING') && calculationRequestData.id == id) {
                              validationOk = true;
                              ErrorService.showError('LABEL_VALIDATION_PENDING_CALCULATION_REQUEST_REJECT');
                              continueLoop = false;
                            }
                          }
                        });

                      });

                    }

                    if (!validationOk) {
                      if (confirm(gettextCatalog.getString('LABEL_JSCRIPT_ALERT_REJECT_CALCULATION_CONFIRMATION'))) {
                        ProvisionAdjustmentService.rejectCalculationRequest(id).success(function() {
                          ErrorService.removeAllErrors();
                          getWorkflows();
                          getIfrsWorkflows();
                        }).error(function(data, status) {
                          getWorkflows();
                          getIfrsWorkflows();
                          ErrorService.showServerErrors(data.messages);
                        });
                      } else {
                        return false;
                      }
                    } else {
                      getWorkflows();
                      getIfrsWorkflows();
                    }
                  };

                  $scope.viewLogs = function(calculationRequestObj, calcReqType) {
                    var calcId = calculationRequestObj.id;
                    if (calcReqType == 1) {
                      var logId;
                      var continueLoop = true;
                      angular.forEach($scope.homePageFormData.workflows.reportingPeriodDataList.reportingPeriodList, function(reportingPeriodData) {
                        angular.forEach(reportingPeriodData.calculationRequestVOList, function(calculationRequestData) {
                          if (continueLoop) {
                            //alert(calculationRequestData.logId);
                            if (calculationRequestData.id == calcId) {
                              logId = calculationRequestData.logId;
                              continueLoop = false;
                            }
                          }
                        });

                      });
                      pasCache.put(backActionKey, '/home');
                      $location.path('/logs/' + logId + '/calculationId/' + calcId + '/calcReqType/' + calcReqType).search({});
                    } else if (calcReqType == 4) {
                      pasCache.put(backActionKey, '/home');
                      $location.path('/calculationId/' + calcId + '/calcReqType/' + calcReqType).search({});
                    }
                  };

                  $scope.mdmVersionHover = function(importDataSetId) {
                    $("[rel=popover]").popover('destroy');
                    var title = "MDM Version";
                    var contentName = [];
                    ProvisionAdjustmentService.getMDMVersionNumber(importDataSetId).success(
                          function(data) {
                            $scope.formData.MDM = [];
                            angular.forEach(data, function(mdmVersion) {
                              if (mdmVersion.mdmVersionNumber != undefined) {
                                $scope.formData.MDM.push({
                                  'mdmDataSetId': mdmVersion.mdmDataSetId,
                                  'domainName': mdmVersion.mdmDomain,
                                  'versionNumber': mdmVersion.mdmVersionNumber
                                });
                              } else {
                                $scope.formData.MDM.push({
                                  'mdmDataSetId': mdmVersion.mdmDataSetId,
                                  'domainName': mdmVersion.mdmDomain,
                                  'versionNumber': 'Unknown'
                                });
                              }
                            });
                            var table_start = "<div class='popovr'>";
                            if ($scope.formData.MDM.length > 0) {
                              for (var i = 0; i < $scope.formData.MDM.length; i++) {
                                contentName.push("<span class='boldfont12 popovrleft'>" + $scope.formData.MDM[i].domainName + "&nbsp;" + "</span>"
                                      + "<span class='popovrright'>" + $scope.formData.MDM[i].versionNumber + "</span>");
                              }
                            } else {
                              contentName.push("<span class='boldfont12 popovrright'>" + "No Version Found" + "</span>");
                            }
                          });
                    var table_start = "</div>";
                    var content = [];
                    content = contentName;
                    $("[rel=popover]").popover({
                      animation: true,
                      content: content,
                      title: title,
                      container: 'body',
                      html: true,
                      trigger: 'hover',
                      placement: 'bottom',
                      delay: {
                        "show": 500,
                        "hide": 100
                      }
                    });

                    $("[rel=popover]").attr('data-content', content);
                    $(this).attr('data-title', title);
                    var popover = $("[rel=popover]").data('bs.popover');
                    popover.setContent();
                    popover.$tip.addClass(popover.options.placement);

                  };

                  $scope.openCreateCalcModal = function() {

                    var validationOk = true;
                    var continueLoop = true;

                    angular.forEach($scope.homePageFormData.workflows.reportingPeriodDataList.reportingPeriodList, function(reportingPeriodData) {
                      angular.forEach(reportingPeriodData.calculationRequestVOList, function(calculationRequestData) {
                        if (continueLoop) {
                          if (calculationRequestData.status == gettextCatalog.getString('LABEL_PENDING')) {
                            continueLoop = false;
                            validationOk = false;
                            alert(gettextCatalog.getString('LABEL_VALIDATION_CALCULATION_REQUEST_PENDING'));
                          }
                          if (calculationRequestData.status == gettextCatalog.getString('LABEL_REPORT_GEN')) {
                            continueLoop = false;
                            validationOk = false;
                            alert(gettextCatalog.getString('LABEL_VALIDATION_CALCULATION_REQUEST_REPORT_GEN'));
                          }
                          if (calculationRequestData.status == gettextCatalog.getString('LABEL_INITIAL')) {
                            continueLoop = false;
                            validationOk = false;
                            alert(gettextCatalog.getString('LABEL_VALIDATION_CALCULATION_REQUEST_INITIAL'));
                          }
                        }
                      });

                    });
                    if (validationOk) {
                      $scope.input.comments = "";
                      $scope.input.selMdlDataSetId = "";
                      $scope.input.selMdmDataSetId = "";
                      $scope.input.selIncreaseDataSetId = "";
                      $scope.input.selCIFDataSetId = "";
                      $scope.input.selIPSDataSetId = "";
                      $scope.modelBasedFlag = false;
                      $scope.loadImportDataSet();
                    }
                  };

                  $scope.loadImportDataSet = function() {
                    ProvisionAdjustmentService.getDataSets().success(function(data) {
                      $scope.formData.listMDM = [];
                      angular.forEach(data.mdmDataSetList, function(mdm) {
                        $scope.formData.listMDM.push({
                          'id': mdm.mdmDataSetId,
                          'name': mdm.mdmDataSetId + " - " + mdm.importDate
                        });
                      });

                      $scope.formData.listMDL = [];
                      angular.forEach(data.mdlDataSetList, function(mdl) {
                        var selreportingPeriod = mdl.reportingPeriod;
                        var name = '';
                        if (mdl.sourceSystem != '') {
                          name = mdl.mdlDataSetId + " - " + selreportingPeriod + " - " + mdl.sourceSystem;
                        } else {
                          name = mdl.mdlDataSetId + " - " + selreportingPeriod + " - MDL";
                        }
                        $scope.formData.listMDL.push({
                          'id': mdl.mdlDataSetId,
                          'name': name,
                          'date': selreportingPeriod,
                          'isArchive': mdl.archive
                        });
                      });

                      $scope.formData.listIncrease = [];
                      angular.forEach(data.increaseDataSetList, function(increase) {
                        var increaseReportingPeriod = increase.reportingPeriod;
                        $scope.formData.listIncrease.push({
                          'id': increase.increaseDatasetId,
                          'name': increase.increaseDatasetId + " - " + increaseReportingPeriod,
                          'date': increaseReportingPeriod
                        });
                      });

                      $scope.formData.listCIF = [];
                      angular.forEach(data.cifDataSetList, function(cif) {
                        var cifReportingPeriod = cif.reportingPeriod;
                        $scope.formData.listCIF.push({
                          'id': cif.importDataSetId,
                          'name': cif.importDataSetId + " - " + cifReportingPeriod,
                          'date': cifReportingPeriod
                        });
                      });

                      $scope.formData.listIPS = [];
                      angular.forEach(data.ipsDataSetList, function(ips) {
                        var ipsReportingPeriod = ips.reportingPeriod;
                        $scope.formData.listIPS.push({
                          'id': ips.datasetId,
                          'name': ips.datasetId + " - " + ipsReportingPeriod,
                          'date': ipsReportingPeriod
                        });
                      });

                      var value = $scope.formData.listMDL[0].date;
                      //var mdlId = $scope.formData.listMDL[0].id;
                      //var mdmId = $scope.formData.listMDM[0].id;
                      //var increaseId = $scope.formData.listIncrease[0].id;
                      //var cifId = $scope.formData.listCIF[0].id;

                      $scope.input.reportingPeriod = value;
                      $scope.input.selMdlDataSetId = $scope.formData.listMDL[0].id;
                      $scope.input.selMdmDataSetId = $scope.formData.listMDM[0].id;
                      //change to check whether status of importdatasetid is archived 
                      $scope.input.isMdlArchive = $scope.formData.listMDL[0].isArchive;
                      $scope.input.selIncreaseDataSetId = $scope.formData.listIncrease[0].id;
                      $scope.input.selCIFDataSetId = $scope.formData.listCIF[0].id;
                      $scope.input.selIPSDataSetId = $scope.formData.listIPS[0].id;

                      $scope.loadReportingPeriod();
                      CalculationService.createCalculationRequest();
                      loadWorkFlowTypeData();
                      $scope.loadMDMVersion();
                      $scope.isRprtPeriodRequired = false;
                    });

                  };
                  /*
                   * $scope.mdlValueChanged = function(){ $scope.loadReportingPeriod(); }
                   */
                  $scope.loadReportingPeriod = function() {
                    //ErrorService.removeAllErrors();
                    var mdlImportDataSetId = $scope.input.selMdlDataSetId;
                    $scope.input.isMdlArchive = $scope.formData.listMDL[0].archive
                    for ( var i in $scope.formData.listMDL) {
                      if ($scope.formData.listMDL[i].id == mdlImportDataSetId) {
                        $scope.input.reportingPeriod = $scope.formData.listMDL[i].date;
                        //change to check whether status of importdatasetid is archived 
                        $scope.input.isMdlArchive = $scope.formData.listMDL[i].isArchive;
                        break;
                      }
                    }
                    //alert($scope.input.reportingPeriod);
                    ProvisionAdjustmentService.getReportingPeriod($scope.input.reportingPeriod).success(function(data) {
                      $scope.formData.listReportingPeriod = [];
                      $scope.formData.listReportingPeriod.push({
                        'id': 0,
                        'name': 'Select'
                      });
                      angular.forEach(data.reportingDateList, function(reportingMonth) {
                        $scope.formData.listReportingPeriod.push({
                          'id': reportingMonth,
                          'name': reportingMonth
                        });
                      });
                      //$scope.input.reportingPeriod = data.reportingPeriodValue;
                      $scope.input.reportingPeriod = 0;
                    });
                  };

                  $scope.loadMDMVersion = function() {
                    ErrorService.removeAllErrors();
                    $scope.displayError = false;
                    var mdmImportDataSetId = $scope.input.selMdmDataSetId;
                    ProvisionAdjustmentService.getMDMVersionNumber(mdmImportDataSetId).success(function(data) {
                      if (data != undefined && data.length > 0) {
                        $scope.formData.isVersionPresent = true;
                      } else {
                        $scope.formData.isVersionPresent = false;
                      }
                      $scope.formData.Version = [];
                      angular.forEach(data, function(mdmVersion) {
                        if (mdmVersion.mdmVersionNumber != undefined) {
                          $scope.formData.Version.push({
                            'mdmVersion': mdmVersion.mdmDomain + '-' + mdmVersion.mdmVersionNumber
                          });
                        } else {
                          $scope.formData.Version.push({
                            'mdmVersion': mdmVersion.mdmDomain + '-' + 'Unknown'
                          });
                        }
                      });
                      if ($scope.isActiveWorkflow != undefined && $scope.isActiveWorkflow == true && data != undefined && data.length != 5) {
                        $scope.displayError = true;
                        ErrorService.showError(gettextCatalog.getString('LABEL_VALIDATION_CP_MDM_NOT_FOUND'));
                        ErrorService.removeAllErrors();
                      }
                    });

                  };

                  $scope.createCalculationRequest = function() {

                    var mdlId = $scope.input.selMdlDataSetId;
                    var mdmId = $scope.input.selMdmDataSetId;
                    var isArchive = $scope.input.isMdlArchive;
                    //var comments = $scope.input.comments;
                    var increaseId = $scope.input.selIncreaseDataSetId;
                    var cifId = $scope.input.selCIFDataSetId;
                    var ipsId = $scope.input.selIPSDataSetId;
                    var modelBasedFlag = 0;

                    if ($scope.modelBasedFlag == true) {
                      modelBasedFlag = 1;
                    } else {
                      modelBasedFlag = 0;
                    }

                    //change to check whether status of importdatasetid is archived 
                    if (isArchive) {
                      if (!confirm("For the selected MDL Id: "
                            + mdlId
                            + ", Assests will be migrated from History to Assets Database before start of Calculation request.Do you want to continue")) {
                        return false;
                      }
                    }
                    var validationErrorKeys = [];
                    var reportingPeriod = $scope.input.reportingPeriod;
                    if (reportingPeriod == 0) {
                      $('#reportingPeriod').addClass('validationFailed');
                      $('#reportingPeriod').focus();
                      validationErrorKeys.push('LABEL_VALIDATION_CALC_REQ_REPORT_PERIOD_REQD');
                      $scope.isRprtPeriodRequired = true;
                    }

                    if (validationErrorKeys.length > 0) {
                      ErrorService.showErrors(validationErrorKeys);
                    } else {
                      var jsonForCreateCalculationRequest = genPasService.populateJsonToPostForCreateCalculationRequestNew($scope.input, mdlId,
                            mdmId, increaseId, cifId, ipsId, modelBasedFlag);
                      //alert(jsonForCreateCalculationRequest);
                      $scope.master = angular.copy(jsonForCreateCalculationRequest);
                      ProvisionAdjustmentService.createCalculationRequest($scope.master).success(function() {
                        CalculationService.closeModal();
                        ErrorService.removeAllErrors();
                        getWorkflows();
                      }).error(function(data, status) {
                        CalculationService.closeModal();
                      });
                    }
                  };

                  // Nilesh Changes Begin
                  $scope.onClickApprovalCheckBox = function(calculationRequestId, calculationRequestStatus) {

                    if (calcRequestMap[calculationRequestId] != undefined || calcRequestMap[calculationRequestId] != null) {
                      delete calcRequestMap[calculationRequestId];
                    } else {
                      calcRequestMap[calculationRequestId] = calculationRequestStatus;
                    }

                    $scope.disableSkipAdjButton = false;
                    $scope.disablePoApproveButton = false;

                    var inLoop = false;
                    for ( var calcReqId in calcRequestMap) {
                      inLoop = true;
                      if (calcRequestMap[calcReqId] != gettextCatalog.getString("LABEL_HOME_STATUS_INITIAL")) {
                        $scope.disableSkipAdjButton = true;
                      }

                      if (calcRequestMap[calcReqId] != gettextCatalog.getString("LABEL_HOME_STATUS_INITIAL")
                            && calcRequestMap[calcReqId] != gettextCatalog.getString("LABEL_HOME_STATUS_ADJ_SKIPPED")
                            && calcRequestMap[calcReqId] != gettextCatalog.getString("LABEL_HOME_STATUS_ADJ_APPROVED")) {
                        $scope.disablePoApproveButton = true;
                      }
                    }

                    if (!inLoop) {
                      $scope.disableSkipAdjButton = true;
                      $scope.disablePoApproveButton = true;
                    }

                  };

                  $scope.disableCheckbox = function(calculationRequestStatus) {
                    if (calculationRequestStatus == gettextCatalog.getString("LABEL_HOME_STATUS_CCC_APPROVE")
                          || calculationRequestStatus == gettextCatalog.getString("LABEL_HOME_STATUS_PO_APPROVE")
                          || calculationRequestStatus == gettextCatalog.getString("LABEL_WORKFLOW_REFR_APPROVE")
                          || calculationRequestStatus == gettextCatalog.getString("LABEL_WORKFLOW_SKIP_REFR_APPROVE")
                          || calculationRequestStatus == gettextCatalog.getString("LABEL_HOME_STATUS_ADJ_CALCULATED")
                          || calculationRequestStatus == gettextCatalog.getString("LABEL_HOME_STATUS_ADJ_SUBMIT")) {
                      return true;
                    }
                  };

                  $scope.workflowSelected = function(calculationRequestId) {
                    if (calcRequestMap[calculationRequestId] != undefined || calcRequestMap[calculationRequestId] != null) {
                      return true;
                    } else {
                      return false;
                    }
                  };

                  $scope.resetCheckboxes = function() {
                    ErrorService.removeAllErrors();
                    for ( var calcReqId in calcRequestMap) {
                      delete calcRequestMap[calcReqId];
                    }
                    $scope.disableSkipAdjButton = true;
                    $scope.disablePoApproveButton = true;
                  };

                  $scope.skipAdjustmentProcessing = function() {

                    if (confirm(gettextCatalog.getString('LABEL_JSCRIPT_ALERT_SKIP_ADJUSTMENT__CONFIRMATION'))) {
                      for ( var calcReqId in calcRequestMap) {
                        if (calcRequestMap[calcReqId] == gettextCatalog.getString("LABEL_HOME_STATUS_INITIAL")) {
                          ErrorService.removeAllErrors();
                          ProvisionAdjustmentService.skipAdjustments(calcReqId).success(function() {
                            skipAdjList.push(calcReqId);
                          });
                          delete calcRequestMap[calcReqId];
                        }
                      }
                      pasCache.put('page_reload_manage_workflow_tab', true);
                      location.reload(true);
                    }
                  };

                  $scope.openPoApprovalModal = function() {
                    var calcRequestsString = "";
                    for ( var calcReqId in calcRequestMap) {
                      var calcStatus = calcRequestMap[calcReqId];
                      if (calcStatus == gettextCatalog.getString("LABEL_HOME_STATUS_INITIAL")
                            || calcStatus == gettextCatalog.getString("LABEL_HOME_STATUS_ADJ_APPROVED")
                            || calcStatus == gettextCatalog.getString("LABEL_HOME_STATUS_ADJ_SKIPPED")) {
                        if (calcRequestsString == "") {
                          calcRequestsString = calcRequestsString + calcReqId;
                        } else {
                          calcRequestsString = calcRequestsString + ", " + calcReqId;
                        }
                        poApproveList.push(calcReqId);
                      }
                    }

                    $scope.input.calcRequestString = calcRequestsString;
                    var options = {
                      backdrop: 'static',
                      keyboard: false
                    };
                    $("#poApproveCalculationModal").css("z-index", "1500");
                    $('#poApproveCalculationModal').modal(options);
                    $('#poApproveCalculationModal').on('shown.bs.modal', function() {
                      $(this).find('.modal-dialog').css({
                        'width': '500'
                      });
                    });
                  };

                  $scope.closePoApprovalWindow = function() {
                    ErrorService.removeAllErrors();
                    $('#poApproveCalculationModal').modal('hide');
                  };

                  $scope.forceCloseApprovalWindow = function() {
                    $('#poApproveCalculationModal').modal('hide');
                  };

                  $scope.poApproveCalculationRequest = function() {
                    ErrorService.removeAllErrors();
                    var str = $scope.input.pocccApprovalReason;
                    if (str == undefined) {
                      str = "";
                    }

                    if ( (str + "").trim().length == 0) {
                      ErrorService.showError('LABEL_VALIDATION_REASONAPPROVAL');
                      $scope.forceCloseApprovalWindow();
                      return;
                    }

                    if ( (str + "").trim().length > genPasService.constants.MAX_LENGTH_APPROVE_REJECT_REASON) {
                      ErrorService.showError('LABEL_VALIDATION_APPOVALREASONMAXLENGTH');
                      $scope.forceCloseApprovalWindow();
                      return;
                    }

                    angular.forEach(poApproveList, function(poApproveCalcReqId) {
                      poApproveForIFRS(poApproveCalcReqId);
                    });

                    $scope.closePoApprovalWindow();
                    pasCache.put('page_reload_manage_workflow_tab', true);
                    location.reload(true);
                  };

                  /**PO Approve IFRS calculationRequest */
                  var poApproveForIFRS = function(selectedCalculationRequest) {
                    var input = {
                      'poApproveReason': $scope.input.pocccApprovalReason
                    };
                    ProvisionAdjustmentService.poApproveForIFRS(input, selectedCalculationRequest).success(function(data, status) {
                    });
                  };
                  // Nilesh Changes End

                  $scope.closeModalWindow = function() {
                    ErrorService.removeAllErrors();
                    CalculationService.closeModal();
                  };

                  $scope.clickLeftArrow = function() {
                    $scope.home.showLeftArrow = false
                    $scope.home.showRightArrow = true;
                    $scope.home.showField = false;
                    $scope.home.showTable = false;
                  };

                  $scope.clickRightArrow = function() {
                    $scope.home.showLeftArrow = true
                    $scope.home.showRightArrow = false;
                    $scope.home.showField = true;
                    $scope.home.showTable = true;
                  };

                  function loadWorkFlowTypeData() {
                    ProvisionAdjustmentService.getWorkFlowTypes().success(function(data, status, headers, config) {
                      for (var i = 0; i < data.wfTypesList.length; i++) {
                        if (data.wfTypesList[i].id == 2) {
                          $scope.isActiveWorkflow = data.wfTypesList[i].active;
                        }
                      }
                    });
                  }

                  $scope.openStageRefreshModal = function(calculationRequest) {
                    $scope.input.rsdComments = "";
                    $scope.input.selRSDDataSetId = "";
                    $scope.loadRSDImportDataSets(calculationRequest);
                  };

                  $scope.loadRSDImportDataSets = function(calculationRequest) {
                    ProvisionAdjustmentService.getRSDImportDataSets().success(function(data) {
                      $scope.formData.listRSD = [];
                      angular.forEach(data.rsdDataSetList, function(rsd) {
                        var selreportingPeriod = rsd.reportingPeriod;
                        var name = '';
                        name = rsd.rsdDataSetId + " - " + selreportingPeriod;
                        $scope.formData.listRSD.push({
                          'id': rsd.rsdDataSetId,
                          'name': name
                        });
                      });
                      $scope.input.selRSDDataSetId = $scope.formData.listRSD[0].id;
                    });
                    //createStageRefreshModal(calculationRequest);
                    getPendingCalRequestForRefresh(calculationRequest);
                  };

                  $scope.onClickSkipRefeshIFRS = function(calculationRequestId) {
                    var buttonValue = confirm("Confirm to Skip Refresh Calculation Request Id : " + calculationRequestId);
                    if (buttonValue == true) {
                      skipStageRefreshForIFRS(calculationRequestId);
                    }
                  };
                  /**Function to update workFlow status to SKIP_REF for IFRS calculation request*/
                  function skipStageRefreshForIFRS(calculationRequestId) {
                    ProvisionAdjustmentService.updateWorkflows(calculationRequestId).success(function(data, status) {
                      closeModal();
                      $window.alert(data + ' for calculation request id ' + calculationRequestId);
                      getIfrsWorkflows();
                    }).error(function(data, status) {
                    });
                  }

                  $scope.skipStageRefresh = function(calculationRequestId) {
                    ProvisionAdjustmentService.updateWorkflows(calculationRequestId).success(function(data, status) {
                      closeModal();
                      $window.alert(data + ' for calculation request id ' + calculationRequestId);
                      getWorkflows();
                    }).error(function(data, status) {
                      closeModal();
                    });
                  }

                  $scope.initiateStageRefresh = function(calculationRequestId) {
                    var rsdId = $scope.input.selRSDDataSetId;
                    //var comments = $scope.input.rsdComments;
                    var jsonForCreateStageRefRequest = genPasService.populateJsonToPostForCreateStageRefRequest($scope.input, rsdId,
                          calculationRequestId);
                    $scope.master = angular.copy(jsonForCreateStageRefRequest);
                    ProvisionAdjustmentService.createStageRefreshReq($scope.master).success(function(data, status) {
                      closeModal();
                      ErrorService.removeAllErrors();
                      $window.alert(data + ' for calculation request id ' + calculationRequestId);
                      getWorkflows();
                    }).error(function(data, status) {
                      closeModal();
                    });
                  };

                  /**private functions starts here**/
                  function createStageRefreshModal(calculationRequest) {
                    if (null != calculationRequest) {
                      $scope.calReqId = calculationRequest.id;
                      $scope.mdlImportDataId = calculationRequest.mdlDataSetId;
                      $scope.mdmImportDataId = calculationRequest.mdmDataSetId;
                      $scope.reportingPeriod = calculationRequest.reportingPeriod;
                      $scope.increaseImportDataId = calculationRequest.increaseDataSetId
                      $scope.cifImportDataId = calculationRequest.cifDataSetId;
                      $scope.ipsImportDataId = calculationRequest.ipsDataSetId;
                      var options = {
                        backdrop: 'static',
                        keyboard: false
                      };
                      $("#createStageRefreshModal").css("z-index", "1500");
                      $('#createStageRefreshModal').modal(options);
                      $('#createStageRefreshModal').on('shown.bs.modal', function() {
                      });
                    }
                  }

                  function closeModal() {
                    $('#createStageRefreshModal').modal('hide');
                  }

                  function getPendingCalRequestForRefresh(calculationRequest) {
                    ProvisionAdjustmentService.getPendingCalRequestRefreshCount(calculationRequest.id).success(
                          function(data, status, headers, config) {
                            $scope.count = data;
                            if ($scope.count > 0) {
                              alert(gettextCatalog.getString('LABEL_VALIDATION_STAGE_REFRESH_CALCULATION_REQUEST_PENDING') + calculationRequest.id);
                            } else {
                              createStageRefreshModal(calculationRequest);
                            }
                          }).error(function(data, status) {
                    });
                  }

                  //**********private function ends**********
                }]);