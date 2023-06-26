<template>
  <div class="centerRegister">
    <div class="black-bg">
      <h4>매장 등록</h4>
      <div class="white-bg">
        <form action="" enctype="multipart/form-data">
          <label for="name">회사명</label>
          <input type="text" id="name" v-model="companyName" /><br />

          <div class="daumAddressContainer">
            <input type="button" @click="execDaumPostcode" value="주소 찾기" class=btn btn-primary style="background-color: #2196f3; color:white; margin-bottom: 1vh;"/><br />
            <input type="text" v-model="postcode" placeholder="우편번호" style="width:100%;"/><br/>
            <input type="text" v-model="address" placeholder="주소" style="width:100%;"/><br />
          </div>

          <label for="address">최종 주소</label>
          <input type="text" id="address" v-model="companyAddress" readonly class="centerAddress_A"/><br />


          <label for="info">매장 소개</label>
          <textarea
            type="text"
            id="info"
            v-model="companyInfo"
            class="centerInfo"
          /><br />

          <label for="phoneNumber">전화번호</label>
          <input
            type="text"
            id="phoneNumber"
            v-model="companyPhoneNumber"
          />
          <br />
            <div class="centerFileContainer">
              <label for="store">사진등록</label>
              <input
              type="file"
              multiple
              name="storeFile"
              id="store"
              accept="image/*"
              @change="centerFileChange"
              />
            </div>
            <p :class="centerImgLength === 0 || centerImgLength ===1 ? 
            'centerImgLengthRed' : 'centerImgLengthBlack'">
              {{centerImgLength === 0 || centerImgLength === 1? "사진은 2장이상 등록해주세요" : 
              centerImgLength+"개의 사진이 등록되었습니다."}}</p>
            <button type="submit" 
            class="centerSubmitBtn"
            @click="centerTotalSubmit"
            :disabled="!(nameChecker && addressChecker && infoChecker && phoneNumber && ImgChecker)">
            등록하기
          </button>
        </form>        
        <button class="centerModifyBtn" @click="openModify">수정하기</button>
    </div>
</div>
  </div>
</template>

<script setup>
import {ref, defineEmits, watch} from 'vue';
import { useRouter } from 'vue-router';
import { debounce } from "lodash";
import axios from 'axios';
import {showCustomAlert} from "../ui/Toast";

//다음 확인중
const postcode = ref('');
const address = ref('');
const detailAddress = ref('');
const extraAddress = ref('');

const execDaumPostcode = () => {
  new window.daum.Postcode({
    oncomplete: (data) => {
      if (extraAddress.value !== '') {
        extraAddress.value = '';
      }
      if (data.userSelectedType === 'R') {
        // 사용자가 도로명 주소를 선택했을 경우
        address.value = data.roadAddress;
      } else {
        // 사용자가 지번 주소를 선택했을 경우(J)
        address.value = data.jibunAddress;
      }

      // 사용자가 선택한 주소가 도로명 타입일 때 참고항목을 조합한다.
      if (data.userSelectedType === 'R') {
        // 법정동명이 있을 경우 추가한다. (법정리는 제외)
        // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
        if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
          extraAddress.value += data.bname;
        }
        // 건물명이 있고, 공동주택일 경우 추가한다.
        if (data.buildingName !== '' && data.apartment === 'Y') {
          extraAddress.value +=
            extraAddress.value !== ''
              ? `, ${data.buildingName}`
              : data.buildingName;
        }
        // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
        if (extraAddress.value !== '') {
          extraAddress.value = `(${extraAddress.value})`;
        }
      } else {
        extraAddress.value = '';
      }
      // 우편번호를 입력한다.
      postcode.value = data.zonecode;
    },
  }).open();
};

watch(address, debounce((current, old)=>{
  if(current !== '' && current.trim() !== ''){
    companyAddress.value = current;
    addressChecker.value = true;
    console.log(companyAddress.value);
  }else{
    addressChecker.value = false;
  }
},200))
const emit = defineEmits(["isModify"]);

const openModify = () => {
    emit("isModify");
}


const router = useRouter();
//리스트로 리다이랙트
const centerRedirect = () => {
    router.push("/center")
}
//url
const url = "http://localhost:8090/";


//저장용 변수 선언
const companyName = ref('');
const companyAddress = ref('');
const companyPhoneNumber = ref('');
const companyInfo = ref('');

//검증용 변수 선언
const nameChecker = ref(false);
const addressChecker = ref(false);
const infoChecker = ref(false);
const phoneNumber = ref(false);
const ImgChecker = ref(false)

watch(companyName, debounce((current, old)=>{
  if(current !== '' && current.trim() !== ''){
    nameChecker.value = true;
  }else{
    nameChecker.value = false;
  }
},200));

watch(companyAddress, debounce((current, old)=>{
  if(current !== '' && current.trim() !== ''){
    addressChecker.value = true;
  }else{
    addressChecker.value = false;
  }
},200));

watch(companyInfo, debounce((current, old)=>{
  if(current !== '' && current.trim() !== ''){
    infoChecker.value = true;
  }else{
    infoChecker.value = false;
  }
},200));

watch(companyPhoneNumber, debounce((current, old)=>{
  if(current !== '' && current.trim() !== ''){
    phoneNumber.value = true;
  }else{
    phoneNumber.value = false;
  }
},200));

const centerImg = ref([]);
const centerImgLength = ref(0);
const centerFileChange = (e) => {
  const files = e.target.files;
  for (let i = 0; i < files.length; i++) {
    centerImg.value.push(files[i]);
  }
  centerImgLength.value = files.length;  
  if(centerImgLength.value >= 2){
    ImgChecker.value = true
  }
};

const centerTotalSubmit = async (e) => {
  e.preventDefault();

  const centerRigiData = new FormData();
  
  for (let i = 0; i < centerImg.value.length; i++) {
    centerRigiData.append("centerImg", centerImg.value[i]);
  }

  centerRigiData.append("name", companyName.value);
  centerRigiData.append("address", companyAddress.value);
  centerRigiData.append("phoneNumber", companyPhoneNumber.value);
  centerRigiData.append("info", companyInfo.value);

  try {
    await axios.post(url + "center/infoInsert", centerRigiData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });    

    //성공 이후 로직
    //초기화
    companyName.value = "";
    companyAddress.value = "";
    companyPhoneNumber.value = "";
    companyInfo.value = "";
    //이미지 처리 초기화
    centerImg.value = [];
    centerRedirect(); 
    showCustomAlert("센터 등록이 완료되었습니다.")    
      
  } catch (error) {
    centerRedirect();    
    showCustomAlert("센터 등록에 실패했습니다, 다시 시도해주세요")
  }
};
</script>

<style scoped>
h1 {
  color: black;
}

.black-bg {
  background-color: #f5f5f5;
  padding: 20px;
  color: #fff;
}

.white-bg {
  background-color: #fff;
  padding: 20px;
}

form {
  display: flex;
  flex-direction: column;
}

form label {
  margin-bottom: 5px;
}

form input[type="text"] {
  margin-bottom: 10px;
  padding: 5px;
}

form input[type="file"] {
  margin-bottom: 10px;
}

form button[type="submit"] {
  background-color: #2196f3;
  color: #fff;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

form button[type="submit"]:hover {
  background-color: #23527c;
}

button {
  margin-top: 10px;
  padding: 5px 10px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

form label {
  margin-bottom: 5px;
  display: block;
  color: #3333339b;
  font-weight: bold;
}

.black-bg h4 {
  color: black;
  text-align: center;
}

.centerModifyBtn {
  background-color: yellow;
  color: black;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  width: 100%;
  transition: background-color 0.3s ease;
}

.centerModifyBtn:hover {
  background-color: #ffffcc;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
}
form button[type="submit"]:disabled:hover {
  background-color: #ccc;
  box-shadow: none;
}
.centerInfo {
  height: 12vh;
}

.centerImgLengthRed{
    color: red;
}

.centerImgLengthBlack{
  color: black;
}


.centerSubmitBtn:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}


.addTrainerImg {
  margin-bottom: 20px;
}

.centerFileContainer label {
  display: inline-block;
  padding: 8px 16px;
  background-color: #2196f3;
  color: #fff;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.centerFileContainer input[type="file"] {
  display: none;
}

.centerFileContainer label:hover {
  background-color: #23527c;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
}


.centerAddress_A{
  border: none;
}
.centerAddress_A:focus{
  border: none;
}


.addressBtn{
  background-color: #2196f3;
  color: #fff;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.daumAddressContainer {
  margin-bottom: 10px;
  padding: 5px;  
}
</style>