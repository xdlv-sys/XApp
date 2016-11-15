function CarSlide(v) {
    this.waitItem = {
        carImageData : "wait.gif",
        index: 0
    };
    this.items = [this.waitItem];

    this.add = function (carParkInfo) {
        carParkInfo.index = this.items.length;
        this.items.splice(this.items.length -1,0,carParkInfo);

    };

    if (v){
        this.add(v);
    }
    this.activeSlide = 0;
}
