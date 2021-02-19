'use strict'

const headers = {
  'Content-Type': 'application/json'
};

const credentials = 'same-origin';

var options = new Vue({
  el: '#options',
  data: {
    options: [],
    ready: false
  },
  methods: {
    vote: function(event) {
      var targetName = event.target.id;
      this.sendVote(targetName);
    },
    sendVote: function(choice) {
      const requestOptions = {
        method: 'POST',
        headers,
        credentials
      };
      fetch(`/vote/${choice}`, requestOptions)
        .then(res => {if (res.ok) {
            console.log('Vote saved');
            this.markChecked(choice);
          } else {
            console.log('Server responded with error');
          }
        });
    },
    markChecked: function(choice) {
      this.options.forEach(item => {
        if (item.name == choice) {
          item.checked = true
        } else {
          item.checked = false;
        }
      });
    }
  },
  mounted: function() {
    fetch(`/vote/options`, {
      method: 'GET',
      headers,
      credentials
    }).then(res => {if (!res.ok) throw new Error('Server responded with error ' + res.status); return res.json()})
      .then(data => {
      if (data.options) {
        data.options.forEach(options => this.options.push({name: options, checked: false}));
      }
      this.ready = true;
      })
      .catch(error => console.log(`Initialize voting failed: ${error.message}`));
  }
});

var results = new Vue({
  el: '#results',
  data: {
    chartData: {'red': 1, 'green': 2, 'blue':0},
    colors: ['#f77', '#7f7', '#77f']
  },
  mounted: function() {
    fetch(`/vote/results`, {
      method: 'GET',
      headers,
      credentials
    }).then(res => {if (!res.ok) throw new Error('Server responded with error ' + res.status); return res.json()})
      .then(data => {
        this.chartData = data;
      }).catch(error => console.log(`Getting results failed: ${error.message}`))
  }
});