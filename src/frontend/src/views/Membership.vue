<template>
  <main class="first">
    <h4>이용권안내! (환불없음.. 구현 할줄모름)</h4>

    <h3>** 등록 **</h3>
    <div>
      아이디<input type="text" v-model="memberId"/> <br>

      <select v-model="monthSelected">
        <option v-for="item in monthSelectList" :value="item.value">{{ item.name }}</option>
      </select>
      몇개월? : {{ monthSelected }}개월
      가격 : {{ monthSelectList.find(item => item.value === monthSelected).price }}원 <br>

      <select v-model="ptSelected">
        <option v-for="item in ptSelectList" :value="item.value">{{ item.name }}</option>
      </select>
      PT횟수 : {{ ptSelected }}회
      가격 : {{ ptSelectList.find(item => item.value === ptSelected).price }}원 <br><br>

      총 가격 :
      {{
        totalPrice = monthSelectList.find(item => item.value === monthSelected).price + ptSelectList.find(item => item.value === ptSelected).price
      }}

      <input class="btn btn-primary" type="submit" @click="submit" value="신청"/>
    </div>

    <div>
      <h1>카카오페이 API 이용하기</h1>
      <form @submit.prevent="submitPayment">
        <button type="submit">카카오페이로 결제하기</button>
      </form>
    </div>


    <h3>** 수정 **</h3>
    <div>
      아이디<input type="text" v-model="memberId"/> <br>

      <select v-model="addMonthSelected">
        <option v-for="item in monthSelectList" :value="item.value">{{ item.name }}</option>
      </select>
      몇개월? : {{ addMonthSelected }} <br>


      <select v-model="addPtSelected">
        <option v-for="item in ptSelectList" :value="item.value">{{ item.name }}</option>
      </select>
      PT횟수 : {{ addPtSelected }}

      <input class="btn btn-primary" type="submit" @click="updateBtn" value="수정"/>
    </div>
    <br><br>
    -----------------------------------
    <div v-for="membership in memberships">
      <div>
        회원ID : {{ membership.memberId }}
      </div>
      <div>
        시작일 : {{ membership.startDate }}
      </div>
      <div>
        종료일 : {{ membership.endDate }}
      </div>
      <div>
        남은 횟수 : {{ membership.remainingPT }}
      </div>
    </div>

  </main>
</template>
<script setup>
import {onMounted, ref} from "vue";
import axios from "axios";

const memberId = ref(1);
const paymentMonths = ref(0);
const remainingPT = ref(0);
const memberships = ref();
const addPaymentMonths = ref(0);

const monthSelected = ref("");
const ptSelected = ref("");
const totalPrice = ref();

const addMonthSelected = ref("");
const addPtSelected = ref("");

const monthSelectList = ref([
  {name: "선택해주세요.", value: ""},
  {name: "1개월", value: 1, price: 50000},
  {name: "3개월", value: 3, price: 150000},
  {name: "6개월", value: 6, price: 300000},
  {name: "12개월", value: 12, price: 600000}
]);

const ptSelectList = ref([
  {name: "선택해주세요.", value: ""},
  {name: "5회", value: 5, price: 250000},
  {name: "10회", value: 10, price: 500000},
  {name: "15회", value: 15, price: 750000},
  {name: "20회", value: 20, price: 1000000}
]);

const submitPayment = () => {
  // 여기에 결제 처리 로직을 추가하세요
  // 예: axios를 사용하여 서버로 결제 요청을 보내고, 응답을 처리하는 등의 작업을 수행합니다
  axios.post("/api/test/membership/pay", {
    memberId: memberId.value,
    totalPrice: totalPrice.value
  }).then((response) => {
    console.log(response.data)
  }).catch((error) => {
    console.log(error)
  });
}


onMounted(() => {
  axios.get("/api/test/membership/items", {}).then((response) => {
    console.log(response.data);
    memberships.value = response.data;
  }).catch((error) => {
    console.log(error)
  });
});
const submit = () => {
  axios.post("/api/test/membership/create", {
    memberId: memberId.value,
    paymentMonths: monthSelected.value,
    remainingPT: ptSelected.value,
    totalPrice: totalPrice.value
  }).then((response) => {
    console.log(response.data)
  }).catch((error) => {
    console.log(error)
  });
}

const updateBtn = () => {
  axios.put(`/api/test/membership/${memberId.value}/update`, {
    memberId: memberId.value,
    addPaymentMonths: addMonthSelected.value,
    remainingPT: addPtSelected.value
  }).then((response) => {
    console.log(response.data)
  }).catch((error) => {
    console.log(error)
  });
}

// const get = () => {
//   const token = Cookies.get("accessToken")
//   axios.post("", {
//     name: "",
//     age: userAge.value.title
//   }, {
//     headers: {
//       Authorization: token
//     }
//   }).then((response) => {
//
//   }).catch((erorr) => {
//     console.log(erorr)
//   })
// }


</script>

<style scoped>
.first {
  color: #fff;
}
</style>