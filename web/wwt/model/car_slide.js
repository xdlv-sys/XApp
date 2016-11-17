function CarSlide(v) {
    this.waitItem = {
        carImageData : "#",
        index: 1,
        text: ''
    };
    this.items = [this.waitItem];
    this.loading = false;

    if (v){
        this.add(v);
    }
    this.activeSlide = 0;
    this.payType = -1;

    this.add = function (carParkInfo) {
        carParkInfo.text = '轻触图片进行支付';
        this.items.splice(this.items.length -1,0, carParkInfo);
        var item = [];

        angular.forEach(this.items, function(v, i) {
            v.index = i;
            item.push(v);
        });

        this.items = item;
        this.loading = false;
        if (carParkInfo.wxPay){
            this.payType = 0;
        }
        if (carParkInfo.aliPay){
            this.payType = 1;
        }
    }
    if (this.aliPay) {
    };
    this.isEnd = function(){
        return this.items.length > 1
            && this.items.length - 1 === this.activeSlide;
    };

    this.getItem = function(index){
        if (this.items[index] === this.waitItem){
            return null;
        }
        return this.items[index];
    };

}
