function CarParkInfo(v){
    this.price = -1;

    if (!v) {
        return;
    }
    var me = this;
    angular.forEach(v, function(v, i) {
        me[i] = v;
    });
    //this.carImageData = 'data:image/jpg;base64,' + this.carImageData;
    this.carImageData = 'pay!sessionImage.cmd?' + v.carNumber;
    var hours = this.consumedTime / 60 | 0;
    var minutes = this.consumedTime % 60;
    this.consumedTimeValue = '' + hours + '小时' + minutes + '分钟';

}
