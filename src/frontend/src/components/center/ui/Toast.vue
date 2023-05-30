<template>
  <div :class="['toast', {'show': isToastVisible}]">{{message}}</div>  
</template>

<script setup>
const props = defineProps({
    message: String,
    isToastVisible: Boolean,
})

</script>

<style scoped>
.toast {
  position: fixed;
  top: 10vh;
  left: 50%;
  transform: translateX(-50%);
  background-color: #333;
  color: #fff;
  padding: 12px 20px;
  border-radius: 4px;
  opacity: 0;
  transition: opacity 0.3s;
}

.show {
  opacity: 1;
}
</style>