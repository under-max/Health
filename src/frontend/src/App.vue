<script setup lang="js">
import { RouterLink, RouterView } from 'vue-router'
import {ref, watchEffect} from 'vue';
import logo from '@/assets/Logo.png';
import Header from './components/Header.vue';
const Login = ref(false);
</script>

<template>
  <Header></Header>
  <RouterView />
</template>

<style>
body{
  margin: 0px;
  background-color: #112470;
  background-image: linear-gradient(176deg, #112470 35%, #fff);
  background-repeat: no-repeat;
  height: 100vh;
}

</style>
