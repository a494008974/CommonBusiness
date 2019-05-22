new Vue({
      el: '#app',
      data: {
        hotels: ''
      },
      created: function () {
        this.$http.get('/hotels').then(response => {
            this.hotels = response.body;
          }, response => {
          });
      },
      method:{

      }
 })
