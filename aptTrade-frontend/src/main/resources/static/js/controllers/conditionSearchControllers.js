'use strict';

angular.module('app')
    .controller('conditionSearchController', function ($scope, addressServices, aptTradeServices) {
    /* ******************************************************** */
        var ct = this;
        // ct.init = true;
        // ct.isCompare = false;
        /*행정구역*/
        ct.sidoList  = [];
        ct.gugunList = [];
        ct.dongList  = [];
        ct.sltSido   = "";
        ct.sltGugun  = "";
        ct.sltDong   = "0";
        /*실거래가 조건*/
        ct.priceRange = [];
        ct.areaRange = [];
        ct.sltPrice = {};
        ct.sltArea = {};
        ct.quarter = 1; //분기
        /*결과*/
    /* ******************************************************** */


        ct.listDongBySidoAndGugun = function(sido, gugun){
            if(ct.sltSido  === "") ct.sltSido = sido;
            if(ct.sltGugun === "") ct.sltGugun = gugun;

            var dongPromise = addressServices.listDongBySidoAndGugun(ct.sltSido, ct.sltGugun);
            dongPromise.success(function (data) {
                ct.dongList = data;
                if( data.length > 0 ){
                    ct.sltDong = ct.dongList[0] == null ? ct.dongList[1] : ct.dongList[0];
                }
                $scope.$parent.maindLoading=false;
            }).error(function () {
                $scope.$parent.maindLoading=false;
            })
        };

        ct.listGugunBySido = function(sido){
            if(sido === ""){
                ct.gugunList = [];
                ct.dongList  = [];
                ct.reset();
                return;
            }
            var gugunPromise = addressServices.listGugunBySido(ct.sltSido);
            gugunPromise.success(function (data) {
                ct.gugunList = data;
                if(data.length > 0){
                    ct.sltGugun  = data[0] == null ? data[1] : data[0];
                    ct.listDongBySidoAndGugun(ct.sltSido, ct.sltGugun);
                }
            }).error(function () {
                $scope.$parent.maindLoading=false;
            })
        };

        /**
         * 행정구역 목록 조회 - 시/도
         */
        ct.listAdmSido = function(){
            $scope.$parent.maindLoading=true;
            var sidoPromise = addressServices.listSido();
            sidoPromise.success(function (data) {
                ct.sidoList = data;
                ct.sltDong="0";
                $scope.$parent.maindLoading=false;
            }).error(function () {
                $scope.$parent.maindLoading=false;
            })
        };

        /* **************************** 조건 검색 ******************************** */

        /**
         * 조건 검색
         */
        ct.conditionSearch = function(){
            ct.isCompare =true;
            if( ct.sltSido === "" || ct.sltGugun === "" || ct.sltDong === "0" ){
                alert("행정구역을 선택하세요.");
                return;
            }
            $scope.$broadcast("conditionClick", {
                sltAdm: ct.sltSido+ "/" +ct.sltGugun + "/" + ct.sltDong,
                sltPrice: ct.sltPrice,
                sltArea: ct.sltArea,
                quarter: ct.quarter
            });

        };
        /* **************************** 조건 검색 ******************************** */

        ct.reset = function(){
            ct.sltSido   = "";
            ct.sltGugun  = "";
            ct.sltDong   = "0";
            ct.priceRange = [
                {"value": "-","name": "실거래가(단위: 만원)"},
                {"value": "10000-30000","name": "1억 ~ 3억 이하"},
                {"value": "30000-50000","name": "3억 ~ 5억 이하"},
                {"value": "50000-80000","name": "5억 ~ 8억 이하"},
                {"value": "90000-",     "name": "9억 이상"}
            ];
            ct.areaRange = [
                {"value": "-","name": "면적(단위: m²)"},
                {"value": "0-30",    "name": "~30m² 이하"},
                {"value": "30-60",  "name": "30m² ~ 60m² 이하"},
                {"value": "60-90",  "name": "60m² ~ 90m² 이하"},
                {"value": "90-120", "name": "90m² ~ 120m² 이하"},
                {"value": "120-",   "name": "120m² ~"}
            ];
            ct.sltPrice = ct.priceRange[0];
            ct.sltArea = ct.areaRange[0];
        };
        ct.reset();
        ct.listAdmSido();
    })
    .controller('conditionSearchResultController', function ($scope,$rootScope ,aptTradeServices) {
        var rs = this;
        rs.templateUrl = '/views/aptResult.html';
        rs.sltAddr = null;
        rs.quarter = 1;
        rs.thisYear = new Date();
        rs.list = [];
        //조건 검색
        rs.isCondition=false;
        rs.sltAdm=null;
        rs.sltPrice="";
        rs.sltArea ="";
        rs.aptList = [];

        $scope.$on("conditionClick", function(event,params) {
            rs.isCondition=true;
            rs.sltAdm = params.sltAdm;
            rs.sltPrice = params.sltPrice;
            rs.sltArea = params.sltArea;
            rs.quarter = params.quarter;
            rs.conditionSearch();
        });

        /**
         * 아파트 실거래가 대비 공시지가 비율 정보 조회 by 상세 조건
         */
        rs.conditionSearch = function(){
            rs.list = [];
            $scope.$parent.$parent.loadingMain=true;
            var promise = aptTradeServices.listByAddressAndQuarter(rs.sltAdm, rs.sltPrice.value , rs.sltArea.value, rs.quarter);
            promise.success(function (data) {
                rs.aptList =data;
                rs.list = data;
                $scope.$parent.$parent.loadingMain=false;
            }).error(function (data) {
                alert("API 요청 실패");
                $scope.$parent.$parent.loadingMain=false;
            })
        };


        /**
         * 공시지가를 조회하기 위한 PNU 자릿수 계산
         * @param code
         * @returns {*}
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
         * 아파트명 선택 시 공시지가 비율 조회
         * @param sltApt 선택된 아파트/주소 정보
         */
        rs.compareByCondition =function (sltApt, addr) {
            rs.isCompare=true;
            rs.sltAddr = addr;
            if(addr === '전체'){
                rs.list = sltApt;
            }else{
                rs.list =[];
                rs.list.push(sltApt);
            }

        };

        rs.settingPlvRowspan = function(apt, index, data){
            data.plvRowspan='';
            var show=false;
            if( angular.isDefined(apt.body[index+1]) ){
                if(apt.body[index+1].avgDeclaredValue === data.avgDeclaredValue){
                    apt.body[index+1].plvSkip=true;
                    data.plvRowspan=rs.calculateRowspan(apt, data, 'plv');
                }
                if(angular.isUndefined(data.plvSkip)){
                    show=true;
                }
            }else{//마지막
                if(angular.isUndefined(data.plvSkip)){
                    show=true;
                }
            }
            return show;
        };

        /**
         * 전용면적 별 rowspan 계산
         * @param index -> row index
         * @param data  -> 실거래가 대비 공시지가 비율 데이터 by row
         * @returns {boolean}
         */
        rs.areaFilter = function(aptList, index, data){
            data.areaRowspan='';
            var show=false;
            if( angular.isDefined(aptList.body[index+1]) ){
                var areaA = (aptList.body[index+1].aptTrade.area).toFixed(2);
                var areaB = (data.aptTrade.area).toFixed(2);
                if(areaA === areaB){
                    aptList.body[index+1].skip=true;
                    data.areaRowspan=rs.calculateRowspan(aptList, data, 'area');
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
         * rowspan 계산
         * @param data  -> 실거래가 대비 공시지가 비율 데이터 by row
         * @param event -> 전용면적/공시지가 구분
         * @returns {number}
         */
        rs.calculateRowspan = function(aptList, data, event){
            var count =0;
            for( var i=0; i< aptList.body.length; i++ ){
                var compareA;
                var compareB;
                if( event === 'area' ){
                    compareA = (aptList.body[i].aptTrade.area).toFixed(2);
                    compareB = (data.aptTrade.area).toFixed(2);
                }else{
                    compareA = (aptList.body[i].avgDeclaredValue);
                    compareB = (data.avgDeclaredValue);
                }
                if(compareA === compareB){
                    count += 1;
                }
            }
            return count;
        };

    })
;
