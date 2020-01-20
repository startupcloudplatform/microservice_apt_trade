'use strict';

angular.module('app')
    .controller('aptSearchController', function ($scope, aptTradeServices, addressServices) {
        /* ********************************************************** */
        var ct = this;
        ct.init=true;
        ct.close = true;
        ct.keyword = ""; //키워드
        ct.alert = {}; //주소 메시지
        ct.addrList = []; //주소 결과 목록
        ct.sltAddr = null;
        ct.isCompare = false;
        ct.pageOptions={
            currentPage : 1,
            pageSize : 15,
            total : 1
        };
        /* ********************************************************** */

        /**
         * 엔터 이벤트
         * @param $event
         * @param currentPage
         */
        ct.enterPress=function($event, currentPage){
            if($event.which === 13){
                ct.listAddressByKeyword(currentPage);
            }
        };

        $scope.closeAlert = function() {
            ct.close =true;
        };


        ct.reset = function(){
            ct.init = true;
            ct.close = true;
            ct.alert = {};
            ct.keyword ="";
            ct.list = [];
            ct.sltAddr= null;
            ct.isCompare =false;
        };

        ct.selected = function(addr) {
            ct.keyword = addr.roadAddr;
            ct.sltAddr = addr;
            ct.listAddressByKeyword(1);

        };

        /**
         * 비교 버튼 이벤트
         */
        ct.compare = function(){
            ct.isCompare =true;
            $scope.$broadcast("addrClick", {
                sltAddr: ct.sltAddr
            })
        };

        /**
         * 아파트 키워드별 실거래가 대시 공시지가 비율 조회
         * @param currentPage 현재 페이지
         */
        ct.listAddressByKeyword = function (currentPage) {
            $scope.$parent.loadingMain=true;

            if (angular.isDefined(currentPage) && currentPage != null) {
                ct.pageOptions.currentPage = currentPage;
            }

            var addressList = addressServices.listByKeyword(currentPage, ct.pageOptions.pageSize, ct.keyword);
            addressList.success(function (data) {
                $scope.$parent.loadingMain=false;
                ct.init = false;
                ct.list = data;
                if( angular.isDefined(data) ){
                    ct.pageOptions.total = data.common.totalCount;
                    if(data.juso == null){
                        ct.close = false;
                        ct.alert = { msg: data.common.errorMessage};
                    }else{
                        ct.close =true;
                    }
                }
            }).error(function () {
                $scope.$parent.loadingMain=false;
                ct.init = false;
                ct.list = [];
            });
        }
    })
    /*  */
    .controller('aptSearchResultController', function ($scope,$rootScope ,aptTradeServices) {
        var rs = this;
        rs.templateUrl = '/views/aptResult.html';
        rs.sltAddr = null;
        rs.quarter = 1;
        rs.thisYear = new Date();
        rs.list = [];

        $scope.$on("addrClick", function(event,params) {
            rs.sltAddr = params.sltAddr;
            rs.quarter = 1;
            rs.thisYear = new Date();
            rs.list = [];
            rs.listAptTradeByApt();
        });

        /**
         * 아파트 실거래가 대비 공시지가 비율 정보 조회 by 아파트명
         */
        rs.listAptTradeByApt = function () {
            if(!rs.sltAddr) return;

            $scope.$parent.$parent.loadingMain=true;
            var pnu = rs.sltAddr.admCd + (Number(rs.sltAddr.mtYn)+1) + rs.setFormatForPnu(rs.sltAddr.lnbrMnnm) + rs.setFormatForPnu(rs.sltAddr.lnbrSlno);
            var promise = aptTradeServices.listByAdrCodeAndQuarter(rs.sltAddr.rnMgtSn, pnu, rs.sltAddr.buldMnnm, rs.sltAddr.buldSlno, rs.quarter);
            promise.success(function (data) {
                if(data.length > 0 ){
                    rs.list =data[0].body;
                }else{
                    rs.list =[];
                }
                $scope.$parent.$parent.loadingMain=false;
            }).error(function (data) {
                alert(data.message);
                $scope.$parent.$parent.loadingMain=false;
            })
        };

        /**
         * Pnu 자릿수 Format
         * @param code 본번/부번
         */
        rs.setFormatForPnu = function(code){
            var format="";
            if(code.length !== 4){ //본번,부번 코드가 4자릿 수
                for(var i=0; i<4-code.length; i++){
                    format += "0";
                }
                format += code;
            }else{
                format = code;
            }
            return format;
        };

        /**
         * 면적 및 공시지가 grouping 하기 위한 rowspan 설정
         * @param data  data 실거래가 대비 공시지가 비율 데이터
         * @param index data index
         */
        rs.filter = function(data, index){
            var show=false;
            data.rowspan='';
            if( angular.isDefined(rs.list[index+1]) ){
                var areaA = (rs.list[index+1].aptTrade.area).toFixed(2);
                var areaB = (data.aptTrade.area).toFixed(2);
                if(areaA === areaB){
                    rs.list[index+1].skip=true;
                    data.rowspan=rs.setRowspan(data);
                }
                if(angular.isUndefined(data.skip)){
                    show=true;
                }
            }else{//마지막
                if(angular.isUndefined(data.skip)){
                    show=true;
                }
            }
            return show;
        };

        /**
         * 면적 및 공시지가 grouping 하기 위한 rowspan 카운트
         * @param data 실거래가 대비 공시지가 비율 데이터
         */
        rs.setRowspan=function(data){
            var count =0;
            for( var i=0; i< rs.list.length; i++ ){
                var compareA = (rs.list[i].aptTrade.area).toFixed(2);
                var compareB = (data.aptTrade.area).toFixed(2);
                if(compareA === compareB){
                    count += 1;
                }
            }
            return count;
        };

    })
;
