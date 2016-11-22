function CarSlide() {

    this.items = [];
    this.loading = false;

    this.activeSlide = 0;
    this.payType = -1;

    this.add = function (carParkInfo) {
        this.parsePayType(carParkInfo);
        //return if contains the same car number
        var index;
        if (this.items.contains(carParkInfo, function (v) {
                if (v.carNumber === carParkInfo.carNumber){
                    v.consumedTimeValue = carParkInfo.consumedTimeValue;
                    v.price = carParkInfo.price;
                    index = v.index;
                    return true;
                }
                return false;
            })) {
            this.activeSlide = index;
            return;
        }
        carParkInfo.index = this.items.length;
        
        this.items.push(carParkInfo);
    };

    this.parsePayType = function(carParkInfo){
        if (carParkInfo.aliPay) {
            this.payType = 1;
        }
        
        var ua = navigator.userAgent.toLowerCase();
        if (carParkInfo.wxPay && 
            ua.match(/MicroMessenger/i) == "micromessenger") {
            this.payType = 0;
        }
    };

    this.nextIfNeed = function(cal){
        if (this.activeSlide === this.items.length - 1){
            if (cal)
            cal(this.activeSlide);
        } else {
            this.activeSlide++;
        }
    };
    this.getActiveItem = function(){
        return this.activeSlide < this.items.length ? this.items[this.activeSlide] : null;
    };
}
