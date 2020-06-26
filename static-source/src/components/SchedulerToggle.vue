<template>
  <div class="jumbotron">
      <h3>Scheduled Attestation</h3>
      <h5>A scheduled message to internal users - to attest their disclaimers to clients have been sent.</h5>
      <br>
      <br>
      <p><strong>Current status: </strong><toggle-button v-model="schedulerIsOn" :sync="true"/> </p>
      <p><strong>Last message time: </strong>{{ lastRunDatetime }} </p>
      
  </div>
</template>

<script>

import axios from 'axios'

export default {
  name: 'SchedulerToggle',
  props: [],
  data: function(){
      return {
          schedulerIsOn: false,
          lastRunDatetime: "No data yet"
      }
  },
  watch: {
      schedulerIsOn: async function(val){
          if (val == true){
            // Toggle on
            try {
                await axios.get("/scheduler/on")
            } catch (e){
                console.log(e)
                alert("Network error")
                this.schedulerIsOn = false // restore the original state
            }
          } else {
            // Toggle off
            try {
                await axios.get("/scheduler/off")
            } catch (e){
                console.log(e)
                alert("Network error")
                this.schedulerIsOn = true // restore the original state
            }

          }
      }
  },  
  mounted: async function(){
      // Check scheduler status
      try {
        var schedulerStatus = await axios.get("/scheduler/is_on")
        console.log(schedulerStatus)
        this.schedulerIsOn = schedulerStatus.data.is_on
      } catch (e){
          console.log(e)
          alert("Network error")
      }

      // Check last run date time
      try {
          var lastDt = await axios.get("/scheduler/lastrun")
          console.log(lastDt)
          if (lastDt.data.lastRunDateTime == null) return

          this.lastRunDatetime = lastDt.data.lastRunDateTime
      } catch (e){
          console.log(e)
          alert("Network error")
      }
  }
}
</script>