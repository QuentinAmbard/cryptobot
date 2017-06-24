var CBOT = CBOT || {};

CBOT.splitDataAndVolume = function (rawData) {
    // split the data set into data and volume
    var data = [],
        volume = [],
        dataLength = rawData.length,
        i = 0;
    for (i; i < dataLength; i += 1) {
        data.push([
            1000 * rawData[i][0], // the date
            rawData[i][1], // open
            rawData[i][2], // high
            rawData[i][3], // low
            rawData[i][4] // close
        ]);

        volume.push([
            1000 * rawData[i][0], // the date
            rawData[i][5] // the volume
        ]);
    }
    return {data: data, volume: volume};
};

CBOT.draw = function (containerId, data) {

    // set the allowed units for data grouping
    groupingUnits = [[
        'week',                         // unit name
        [1]                             // allowed multiples
    ], [
        'month',
        [1, 2, 3, 4, 6]
    ]],

        console.log(data)

// create the chart
    Highcharts.stockChart(containerId, {

        rangeSelector: {
            selected: 1
        },

        title: {
            text: 'AAPL Historical'
        },

        yAxis: [{
            labels: {
                align: 'right',
                x: -3
            },
            title: {
                text: 'data'
            },
            height: '60%',
            lineWidth: 2
        }, {
            labels: {
                align: 'right',
                x: -3
            },
            title: {
                text: 'Volume'
            },
            top: '65%',
            height: '35%',
            offset: 0,
            lineWidth: 2
        }],

        tooltip: {
            split: true
        },

        series: [{
            type: 'candlestick',
            name: 'AAPL',
            data: data.data,
            dataGrouping: {
                enabled: false
                //units: groupingUnits
            }
        }, {
            type: 'column',
            name: 'Volume',
            data: data.volume,
            yAxis: 1,
            dataGrouping: {
                enabled: false
                //units: groupingUnits
            }
        }]
    });
}
