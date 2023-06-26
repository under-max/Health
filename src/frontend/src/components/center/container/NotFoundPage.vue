<template>
  <div class="errorContainer">
    <img :src="ErrorPage"/>
  </div>
</template>

<script setup>
import ErrorPage from '@/assets/404.jpg';

</script>

<style scoped>
.errorContainer{  
  background: white;
}
.errorContainer img{
  margin-left: 100px;
  margin-right: auto;
  max-width: 90vw;
  min-width: 90vw;
  width: 90vw;
  height: 92vh;
  max-height: auto;
}
</style>