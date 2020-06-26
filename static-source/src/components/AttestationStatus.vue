<template>
  <div class="jumbotron">
      <h3>Attestation Status</h3>
      <br>
      <br>

        <table class="table table-hover">
        <thead>
            <tr>
            <th scope="col">SID</th>
            <th scope="col">Attested?</th>
            <th scope="col">Attest Start date</th>
            <th scope="col">Attest End date</th>
            </tr>
        </thead>
        <tbody>
            <tr v-for="item in attestations" :key="item.attestStartDatetime" :class="item.rowClass">
                <th scope="row">{{item.userId}}</th>
                <td>{{!!item.attested}}</td>
                <td>{{item.attestStartDatetime}}</td>
                <td>{{item.attestEndDatetime}}</td>
            </tr>
        </tbody>
        </table>


      <!-- <ul id="example-1">
        <li v-for="item in attestations" :key="item.attestStartDatetime">
            {{ item.userId }}
            {{  !!item.attested }}
            {{  item.attestStartDatetime }}
        </li>
    </ul> -->
  </div>
</template>

<script>

import axios from 'axios'

export default {
  name: 'AttestationStatus',
  props: [],
  data: function(){
      return {
          attestations: []
      }
  },
  watch: {
  },  
  mounted: async function(){
      // Check scheduler status
      try {
        var att = []
        var res = await axios.get("/attestation/current")
        for (var x of Object.values(res.data)){
            x.rowClass = x.attested? "": "table-danger"
            att.push(x)
        }

        this.attestations = att.sort((a, b) => {
            if (a.attestStartDatetime > b.attestStartDatetime) return -1
            if (a.attestStartDatetime == b.attestStartDatetime) return 0
            return 1
        })

        
      } catch (e){
          console.log(e)
          alert("Network error")
      }

  }
}
</script>