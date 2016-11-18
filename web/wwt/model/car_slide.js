function CarSlide(v) {
    this.waitItem = {
        carImageData: "#",
        index: 1,
        text: ''
    };
    this.items = [];
    this.loading = false;

    if (v) {
        this.add(v);
    }
    this.activeSlide = 0;
    this.payType = -1;

    this.add = function (carParkInfo) {
        //return if contains
        if (this.items.contains(carParkInfo, function (v) {
                if (v.carNumber === carParkInfo.carNumber){
                    v.consumedTimeValue = carParkInfo.consumedTimeValue;
                    v.price = carParkInfo.price;
                    return true;
                }
                return false;
            })) {
            return;
        }
        carParkInfo.index = this.items.length;
        this.activeSlide = carParkInfo.index;
        this.items.push(carParkInfo);
        this.parsePayType(carParkInfo);
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
            cal(this.activeSlide);
        } else {
            this.activeSlide++;
        }
    };
}
