<template>

  <div>
    <ul class="nav justify-content-center">
      <li class="nav-item">
        <router-link class="nav-link" to="/community">전체</router-link>
      </li>
      <li class="nav-item">
        <router-link class="nav-link" to="/community/notice">공지사항</router-link>
      </li>
    </ul>
  </div>

</template>

<script setup>
import {RouterLink} from "vue-router";

</script>

<style scoped>

</style>