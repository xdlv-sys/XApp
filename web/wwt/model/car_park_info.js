function CarParkInfo(v){
    this.price = -1;

    if (!v) {
        return;
    }
    var me = this;
    angular.forEach(v, function(v, i) {
        me[i] = v;
    });
    this.carImageData = 'data:image/jpg;base64,' + this.carImageData;

    var hours = this.consumedTime / 60 | 0;
    var minutes = this.consumedTime % 60;
    this.consumedTimeValue = '' + hours + '小时' + minutes + '分钟';

    this.payTypes = [];
    if (this.wxPay) {
        this.payTypes.push({ type: 'wx', name: '微信支付' });
    }
    if (this.aliPay) {
        this.payTypes.push({ type: 'ali', name: '支付宝' });
    }
    if (this.payTypes.length > 0) {
        this.selectedPay = this.payTypes[0];
    }
}
