Ext.define('XApp.view.Report', {
    extend: 'Ext.Container',
    xtype: 'report',
    requires:['Ext.dataview.List'],
    config: {
        fullscreen: true,
        layout: 'vbox',
        style: 'background: white',

        items: [{
            xtype: 'chart',
            background: "none",
            store: Ext.create('XApp.store.Report',{
                autoLoad: true
            }),
            animate: true,
            interactions: ['panzoom', 'itemhighlight'],
            legend: {
                position: "bottom"
            },
            series: [{
                    type: 'line',
                    xField: 'city',
                    yField: 'failCount',
                    title: '百分比',
                    style: {
                        smooth: true,
                        stroke: '#115fa6',
                        lineWidth: 3,
                        shadowColor: 'rgba(0,0,0,0.7)',
                        shadowBlur: 10,
                        shadowOffsetX: 3,
                        shadowOffsetY: 3
                    },
                    highlightCfg: {
                        scale: 2
                    },
                    marker: {
                        type: 'circle',
                        stroke: '#0d1f96',
                        fill: '#115fa6',
                        lineWidth: 2,
                        radius: 4,
                        shadowColor: 'rgba(0,0,0,0.7)',
                        shadowBlur: 10,
                        shadowOffsetX: 3,
                        shadowOffsetY: 3,
                        fx: {duration: 300}
                    }
                }, {
                    type: 'bar',
                    xField: 'city',
                    yField: ['rightCount'],
                    title: ['人数'],
                    style: {
                        maxBarWidth: 15,
                        lineWidth: 1.5,
                        fill: "#a61120",
                        stroke: 'black',
                        shadowColor: 'rgba(0,0,0,0.7)',
                        shadowBlur: 10,
                        shadowOffsetX: 3,
                        shadowOffsetY: 3
                    }
                } ],
            axes: [
                {
                    type: 'numeric',
                    position: 'left',
                    grid: {
                        odd: {
                            fill: '#fafafa'
                        }
                    },
                    style: {
                        axisLine: false,
                        estStepSize: 20,
                        stroke: '#ddd'
                    },
                    minimum: 0,
                    maximum: 100
                },
                {
                    type: 'category',
                    position: 'bottom',
                    visibleRange: [0, 0.7],
                    style: {
                        estStepSize: 1,
                        stroke: '#999'
                    }
                }
            ],
            flex: 1
        },{
            flex: 1,
            xtype: 'list',
            store: Ext.create('XApp.store.Report',{
                autoLoad: true
            }),
            itemTpl: '{city}，总数:{rightCount}，比例：{failCount}%'
        }]
    }
});
