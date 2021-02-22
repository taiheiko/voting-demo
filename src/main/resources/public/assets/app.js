'use strict';

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
            this.markChecked(choice);
            results.fetchResults();
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
        data.options.forEach(option => this.options.push({name: option, checked: data.choice === option}));
      }
      this.ready = true;
      })
      .catch(error => console.log(`Initialize voting failed: ${error.message}`));
  }
});

var results = new Vue({
  el: '#results',
  data: {
    chartData: {},
    colors: [],
    colorSchema: {'RED': '#f77', 'GREEN': '#7f7', 'BLUE': '#77f'},
    ready: function() {
      return Object.keys(this.chartData).reduce((acc, key) => acc + this.chartData[key], 0) > 0;
    }
  },
  methods: {
    fetchResults: function() {
     fetch(`/vote/results`, {
       method: 'GET',
       headers,
       credentials
     }).then(res => {if (!res.ok) throw new Error('Server responded with error ' + res.status); return res.json()})
       .then(data => {
         this.chartData = data;
         this.colors = Object.keys(data).map(c => this.colorSchema[c]);
       }).catch(error => console.log(`Getting results failed: ${error.message}`));
   }
  },
  mounted: function() {
    this.fetchResults();
  }
});