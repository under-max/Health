<template>
  <div class="centerRegisterContainer">
    <div class="centerRegister">
        <CenterRegister/>
    </div>
    <div class="centerModify">

    </div>
  </div>
</template>

<script setup>
import CenterRegister from '../components/center/container/CenterRegister.vue';

</script>

<style scoped>
h1 {
  color: black;
}

.centerRegisterContainer {
  min-height: 91.2vh;
  height: auto;
  background: white;
}

.centerRegister {
  margin: 0 auto;
  padding: 20px;
  min-width: 600px;
  width: 65%;
}
</style>