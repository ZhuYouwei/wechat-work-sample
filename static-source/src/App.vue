<template>
  <div id="app" class="container">
    <h1>Shout It Out - Control Panel</h1>
    <hr>
    <SchedulerToggle />
    <AttestationStatus />
  </div>
  
</template>

<script>

import SchedulerToggle from "./components/SchedulerToggle"
import AttestationStatus from "./components/AttestationStatus"

export default {
  name: 'App',
  components: {
    SchedulerToggle,
    AttestationStatus
  }
}
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
