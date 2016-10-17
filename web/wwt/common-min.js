app.config(["$httpProvider",function(a){a.defaults.transformRequest=function(c){var d=[];for(var b in c){d.push(encodeURIComponent(b)+"="+encodeURIComponent(c[b]))}return d.join("&")};a.defaults.headers.put["Content-Type"]="application/x-www-form-urlencoded;charset=utf-8";a.defaults.headers.post={"Content-Type":"application/x-www-form-urlencoded;charset=utf-8"}}]);app.config(["$locationProvider",function(a){a.html5Mode({enabled:true,requireBase:false})}]);app.service("common",["$uibModal","$http","$interval",function(a,c,b){this.isBlank=function(d){return(d===null)||(d===undefined)||(d==="")||(Array.isArray(d)&&d.length===0)};this.copy=function(d,e){d=d||{};if(e){angular.forEach(e,function(f,g){d[g]=f})}},this.error=function(d){this.open({title:"错误",message:d})};this.info=function(d){this.open({title:"提示",message:d})};this.wait=function(d){this.closeWait();this.waitInstance=this.open({title:d?d:"请稍等",tid:"waitDialog.html"})};this.closeWait=function(){if(this.waitInstance){this.waitInstance.close()}};this.open=function(f,d){var e=angular.copy(f);this.copy(e,d);return a.open({animation:true,ariaLabelledBy:"modal-title",ariaDescribedBy:"modal-body",windowClass:"center-modal",templateUrl:f.tid?f.tid:"modalContent.html",controller:"modalInstanceCtrl",controllerAs:"$modalCtl",size:f.size?f.size:"lg",resolve:{conf:function(){return e}}})};this.post=function(e,j,i,d){var h={};angular.forEach(j,function(k,l){if(!angular.isFunction(k)){h[l]=k}});var g=this;var f=!d||!d.tip||d.tip===true;if(f){g.wait()}c.post(e,h).success(function(k){if(f){g.closeWait()}i(k)}).error(function(k){if(f){g.closeWait()}if(!d.error){d.error(k)}else{this.error("网络或服务器错误，请重试")}})};this.interval=function(e,d,f){var g=b(function(){e(function(){b.cancel(g)})},d,f);return g}}]);app.controller("modalInstanceCtrl",["$uibModalInstance","conf",function(c,a){this.ok=function(){c.close();if(this.okAction){this.okAction()}};this.cancel=function(){c.dismiss("cancel");if(this.cancelAction){this.cancelAction()}};var b=this;angular.forEach(a,function(d,e){b[e]=d})}]);app.directive("countDown",["$interval",function(a){return{restrict:"A",link:function(c,e,d,b){c.seconds=parseInt(d.mins)*60;c.promise=a(function(){c.seconds--;if(c.seconds===0){a.cancel(c.promise)}var f=c.seconds/3600|0;var h=c.seconds%3600/60|0;var f=c.seconds/3600|0;var g=c.seconds%60;e.text(f+":"+h+":"+g)},1000)}}}]);