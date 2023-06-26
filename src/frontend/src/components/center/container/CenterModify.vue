<template>
  <DeleteModal v-if="centerDeleteModal"
                :centerDeleteModal="centerDeleteModal"
                @centerDeleteModalBtn="centerDeleteModalBtn"
                :deleteCenterModalChecker="deleteCenterModalChecker"
                @deleteCenterProps="deleteCenterProps"/>
  <div class="modify-black">
    <h4 class="centerModifyTitle">센터 수정</h4>
    <div class="modify-white">
        <div class=centerSearchBox>
            <label>센터검색</label>
            <input v-model="centerName" @keydown.enter="centerSearchBtn"/>
            <button @click="centerSearchBtn">센터찾기</button>
            <br>
        </div>    

      <div v-if="dataSuccess && centerGetData !== null" class="center-info">
        <div v-for="(centerData, index) in centerGetData" :key="index">
          <div v-if="centerData.id === centerSelect">
            <form enctype="multipart/form-data" class="center-form">

              <label for="centerName">매장명</label>
              <input id="centerName" v-model="centerModifyName" />
              
              <div class="daumAddressContainer">
                <input type="button" @click="execDaumPostcode" value="주소 찾기" class=btn btn-primary style="background-color: #2196f3; color:white; margin-bottom: 1vh;"/><br />
                <input type="text" v-model="postcode" placeholder="우편번호" style="width:100%;" readonly/><br/>
                <input type="text" v-model="address" placeholder="주소" style="width:100%;" readonly/><br />
              </div>

              <label for="centerAddress">주소</label>
              <input id="centerAddress" v-model="centerModifyAddress" readonly style="border: none;"/>

              <label for="centerInfo">정보</label>
              <input id="centerInfo" v-model="centerModifyInfo" />

              <label for="centerPhoneNumber">전화번호</label>
              <input
                id="centerPhoneNumber"
                v-model="centerModifyPhoneNumber"
              /><br />

              <div v-for="(imgData, index) in centerData.fileName" :key="index">
                <div v-if="imgData != null" class="ImgDiv">
                  <div :class="fileNameCheckBoolean[index] ? 'removeImgRed' : 'removeImgBlue'"
                    @click="originalFileName(imgData, index)">
                    <i class="fa-regular fa-trash-can">{{fileNameCheckBoolean[index] ? '  삭제 예정인 이미지 입니다' : ''}}</i>                    
                  </div>                  
                  <img
                    :src="`https://bucket0503-1000011011001.s3.ap-northeast-2.amazonaws.com/health/${centerData.id}/${imgData}`"
                  />                  
                </div>
              </div>

              <div id="fileSubmitInputDiv">
                <label for="addFile">사진 등록</label>
                <input
                  id="addFile"
                  type="file"
                  multiple
                  accept="image/*"
                  @change="centerImage"
                  class="fileSubmitInput"
                />
                <p>{{uploadImgLength+"개의 파일이 선택 되었습니다."}}</p>
                <p class="paRed">
                  {{originalFileLength >= 2 ? "" :"수정 후 2개이상의 파일이 있어야 합니다. 현재"+originalFileLength}}
                </p>
              </div>
            </form>
            
                          <button @click="modifyCenter" 
                          :disabled="!(totalCenterModifyResult && originalFileLength >= 2)"
                          class="centerModifySubmitBtn">수정하기</button>
          </div>
        </div>
      </div>

      <div class="otherBtn">
        <button v-if="centerLength !== 0" @click="deleteCenterBtn" class="deleteCenterBtn">삭제</button>
        <button @click="openModify">취소</button>
      </div>

    </div>
  </div>


    <!-- 검색 모달창 -->
    <div v-if="modalOpen" class="modal">
      <div class="modal-content">
        <span class="close" @click="closeModalBtn">&times;</span>
        <h3 class="modal-title">검색 결과</h3>
        <ul class="search-results">
          <li v-for="(data, index) in centerGetData" :key="index" class="search-result" @click="checkCenterIdBtn(data.id)">{{ data.name }}</li>
        </ul>
      </div>
    </div>
</template>

<script setup>
import { ref, watch, defineEmits, defineProps, watchEffect } from 'vue';
import { debounce } from "lodash";
import { useRouter } from 'vue-router';
import {showCustomAlert} from "../ui/Toast";
import axios from 'axios';

import DeleteModal from '../ui/DeleteModal.vue';

//주소 다음 api
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



const props = defineProps({
    isRegister: Boolean,
})
const emit = defineEmits(["isModify"]);

const openModify = () => {
    emit("isModify");    
}

//router
const router = useRouter();
//리스트로 리다이랙트
const centerRedirect = () => {
    router.push("/center")
}


//url
const url = "http://localhost:8090/";

//삭제 모달창 구현
const centerDeleteModal = ref(false);

const centerDeleteModalBtn = ()=>{
  centerDeleteModal.value = false;
}

const deleteCenterProps = () => {
  deleteCenter();
}

const deleteCenterBtn = () => {
  if(centerName.value === ""){
    showCustomAlert("수정하려는 센터를 먼저 검색해 주세요");
  }else{
    centerDeleteModal.value = true;
  }
}
//centerDeleteModal 확인용
const deleteCenterModalChecker = ref(true);



//모달 확인중
const modalOpen = ref(false);

const closeModalBtn = () => {
    modalOpen.value = false;
}

const openModalBtn = () => {
    if(centerGetData.value.length !== 0){
        modalOpen.value = true;
    }
}

const removeFileLength = ref(0);

const originalFileName = (fileName, index) => {  
  fileNameCheckBoolean.value[index] = !fileNameCheckBoolean.value[index];
  if(fileNameCheckBoolean.value[index] === true){
    centerModifyFileName.value[index] = fileName;
  }else{
    centerModifyFileName.value[index] = "";
  }  
  if(centerModifyFileName.value[index] !== "" && fileNameCheckBoolean.value[index] === true){
    removeFileLength.value += 1;
    originalFileLength.value = originalFileLength.value - 1    
  }else if(centerModifyFileName.value[index] === "" && fileNameCheckBoolean.value[index] === false){
    removeFileLength.value -= 1;
    originalFileLength.value = originalFileLength.value + 1
  }
}


//Json용 data
const modifyData = ref({
  id: "",
  name: "",
  address: "",
  phoneNumber: "",
  info: "",
  fileName: [],
})

//처리용 Data
const centerModifyId = ref(0);
const centerModifyName = ref("");
const centerModifyAddress = ref("");
const centerModifyPhoneNumber = ref("");
const centerModifyInfo = ref("");
const centerModifyFileName = ref([]);

//UI용 Boolean
const totalCenterModifyResult = ref(true);
const isOk = ref({
  centerModifyId: true,
  centerModifyName: true,
  centerModifyAddress: true,
  centerModifyPhoneNumber: true,
  centerModifyInfo: true
})
//null값 방지 watch 처리 
watch(centerModifyName, debounce((current, prev)=>{
  const check = current.trim();
  if(check !== ""){
    isOk.value.centerModifyName = true;    
  }else{
    isOk.value.centerModifyName = false;    
  }
  totalCenterModifyResult.value = isOk.value.centerModifyName && isOk.value.centerModifyAddress && isOk.value.centerModifyPhoneNumber && isOk.value.centerModifyInfo;
},200));

watch(address, debounce((current, prev)=>{
  centerModifyAddress.value = current;
  
}, 200))

watch(centerModifyAddress, debounce((current, prev)=>{
  const check = current.trim();
  if(check !== ""){
    isOk.value.centerModifyAddress = true;
  }else{
    isOk.value.centerModifyAddress = false;
  }
  totalCenterModifyResult.value = isOk.value.centerModifyName && isOk.value.centerModifyAddress && isOk.value.centerModifyPhoneNumber && isOk.value.centerModifyInfo;
  
},200));

watch(centerModifyPhoneNumber, debounce((current, prev)=>{
  const check = current.trim();
  if(check !== ""){
    isOk.value.centerModifyPhoneNumber = true;
  }else{
    isOk.value.centerModifyPhoneNumber = false;
  }
  totalCenterModifyResult.value = isOk.value.centerModifyName && isOk.value.centerModifyAddress && isOk.value.centerModifyPhoneNumber && isOk.value.centerModifyInfo;
},200));

watch(centerModifyInfo, debounce((current, prev)=>{
  const check = current.trim();
  if(check !== ""){
    isOk.value.centerModifyInfo = true;
  }else{
    isOk.value.centerModifyInfo = false;
  }
  totalCenterModifyResult.value = isOk.value.centerModifyName && isOk.value.centerModifyAddress && isOk.value.centerModifyPhoneNumber && isOk.value.centerModifyInfo;
},200));

// 아이디 확인해서 가져온 값으로 초기화
const centerSelect = ref(0);
const fileNameCheckBoolean = ref([]);


const originalFileLength = ref(0); //검증용 file 갯수
watch(centerSelect, (newValue, oldValue)=>{
    centerGetData.value.map(data=>{              
        if(data.id === newValue){
            centerModifyId.value = data.id;
            centerModifyName.value = data.name;
            centerModifyAddress.value= data.address;
            centerModifyPhoneNumber.value = data.phoneNumber;
            centerModifyInfo.value = data.info;



            originalFileLength.value = data.fileName.length;
            for(let i = 0; i < data.fileName.length; i++){
              fileNameCheckBoolean.value.push(false);
            }
            for(let i = 0; i < data.fileName.length; i++){
              centerModifyFileName.value.push("");
            }                       
        }
    })    
})

//몇개 찾았는지 확인 이후 Toast 에 넘겨줄 예정
const centerLength = ref(0);

//찾을 센터 이름
const centerName = ref("");

//검색 완료 후 값 담을 변수
const centerGetData = ref([]);

//데이터 다 받아졌는지 확인
const dataSuccess = ref(false);


//센터 검색 axios 로직
const centerSearchBtn = async (e) => {
    e.preventDefault();
  try {
    const response = await axios.get(url + "center/searchname", {
      params: {
        centerName: centerName.value,
      },
    });
    centerGetData.value = response.data;
    dataSuccess.value = true;    
    centerLength.value = centerGetData.value.length;
    let message = centerLength.value + "개의 결과를 찾았습니다.";
    showCustomAlert(message);
    if(centerGetData.value.length !== 0){
        openModalBtn();
    }
  } catch (error) {
    console.log(error);
    dataSuccess.value = false;
  }
};

//선택한 Id확인
const checkCenterIdBtn = (id) => {    
    centerSelect.value = id;
    closeModalBtn();
}


//수정 구현 로직

// checkBox 이미지 입력
const checkBoxImg = ref([]);

watch(checkBoxImg, (currentData, oldData)=>{    
    modifyData.value.fileName = currentData;
})




//업로드한 파일 처리 
const uploadImgLength = ref(0);
const imgData = ref([]);
const centerImage = (e) => {
    const files = e.target.files;    
    for (let i = 0; i < files.length; i++){
        imgData.value.push(files[i]);
    }
    uploadImgLength.value = imgData.value.length;
    originalFileLength.value = originalFileLength.value + uploadImgLength.value;
}

const modifyCenter = async(e)=>{
    e.preventDefault();
    
    const centerModify = new FormData();
    // img data 추가
    modifyData.value.id = centerModifyId.value;
    modifyData.value.name = centerModifyName.value;
    modifyData.value.address = centerModifyAddress.value;
    modifyData.value.phoneNumber = centerModifyPhoneNumber.value;
    modifyData.value.info = centerModifyInfo.value;
    

    for (let i = 0; i < imgData.value.length; i++){
      centerModify.append("addImg", imgData.value[i]);      
    }
    centerModify.append("id", centerModifyId.value);
    centerModify.append("name", centerModifyName.value);
    centerModify.append("address", centerModifyAddress.value);
    centerModify.append("phoneNumber", centerModifyPhoneNumber.value);
    centerModify.append("info", centerModifyInfo.value);
    
    //지울 이미지 처리
    for(let i = 0; i < centerModifyFileName.value.length; i++){
      if(centerModifyFileName.value[i] !== ""){
        modifyData.value.fileName.push(centerModifyFileName.value[i]);
      }      
    }
    for(let i = 0; i < modifyData.value.fileName.length; i++){
      centerModify.append("removeImg", modifyData.value.fileName[i]);      
    }

        
    try{
        await axios.put(url+"center/modify",centerModify,{
            headers:{
                "Content-Type": "multipart/form-data",
            }
        });        
        centerModify.delete("removeImg");
        centerModify.delete("addImg");
        centerRedirect();
        showCustomAlert("센터 수정에 성공하였습니다.")
    }catch(error){        
        showCustomAlert("센터 수정에 실패하였습니다. 다시 시도해 주세요")
    }
}



//삭제 구현 로직
const deleteCenter = async() => { 
    
    modifyData.value.id = centerModifyId.value;
    modifyData.value.name = centerModifyName.value;
    modifyData.value.address = centerModifyAddress.value;
    modifyData.value.phoneNumber = centerModifyPhoneNumber.value;
    modifyData.value.info = centerModifyInfo.value;

    try{
        await axios.delete(url+"center/delete", {data:modifyData.value})

        centerRedirect();
        showCustomAlert("센터삭제에 성공하였습니다.")
    }catch(error){
        console.log(error);
        showCustomAlert("센터삭제에 실패했습니다, 다시 시도해주세요")
    }
}
</script>

<style scoped>
.ImgDiv p{
  color: red;
}
i{
  color: black;
}
label{
  color: black;
}
.modify-black {
  background-color: #f5f5f5;
  padding: 20px;
  color: #fff;
}

.modify-white {
  background-color: #fff;
  padding: 20px;
}

.centerSearchBox input{
  width: 100%;
  padding: 5px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.centerSearchBox label{
  font-weight: bold;  
}

.centerModifyTitle {
  color: black;
  text-align: center;
}

.modify-white form {
  margin-bottom: 20px;
}

.modify-white label {
  display: block;
  margin-bottom: 5px;
}

.modify-white input[type="text"],
.modify-white input[type="file"],
.modify-white textarea {
  width: 100%;
  padding: 5px;
  border: 1px solid #ccc;
  border-radius: 5px;
  margin-bottom: 10px;
}

.modify-white textarea {
  resize: vertical;
}

.modify-white img {
  max-width: 100%;
  height: auto;
  margin-bottom: 10px;
}

.modify-white hr {
  margin: 20px 0;
  border: none;
  border-top: 1px solid #ccc;
}

.modify-white button {
  background-color: #2196f3;
  color: #fff;
  padding: 10px 20px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  margin-right: 10px;
  transition: background-color 0.3s ease;
}

.modify-white button:hover {
  background-color: #23527c;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
}



.modal {
  position: fixed;
  top: 0;
  left: 0;
  z-index: 9999;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background-color: #fff;
  width: 400px;
  padding: 20px;
  border-radius: 10px;
  max-height: 80vh;
  overflow-y: auto;
}

.close {
  position: absolute;
  top: 10px;
  right: 10px;
  font-size: 20px;
  color: #999;
  cursor: pointer;
}

.modal-title {
  margin-bottom: 10px;
}

.search-results {
  list-style-type: none;
  padding: 0;
  margin: 0;
}

.search-result {
  margin-bottom: 5px;
}

.otherBtn .deleteCenterBtn{
  background-color: red;
}

.otherBtn .deleteCenterBtn:hover {
  background-color: lightcoral;
}


form button[type="button"],
form button[type="submit"],
button {
    width: 100%;
    background-color: #2196f3;
    color: #fff;
    padding: 10px 20px;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    margin-top: 10px;
    transition: background-color 0.3s ease;
}

form button[type="button"]:hover,
form button[type="submit"]:hover,
button:hover {
  background-color: #23527c;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.2);
}

.center-info {
  margin-top: 20px;
}

.center-form {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-bottom: 10px;
}

.center-form label {
  font-weight: bold;
}

.center-form input {
  padding: 5px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.ImgDiv img{
  width: 100%;
  height: auto;
}

#fileSubmitInputDiv .fileSubmitInput{
  border: none;
}


.removeImgBlue i{
  color:blue;
    
}

.removeImgRed i{
  color:red;
}

#fileSubmitInputDiv p{
  color: black;
  margin-bottom: 2px;
}

#fileSubmitInputDiv .paRed{
  color: red;
  margin-bottom: 0px;
}

.centerModifySubmitBtn:disabled:hover{
  background-color: #ccc;
}
.centerModifySubmitBtn:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}


#fileSubmitInputDiv {
  margin-bottom: 20px;
}

#fileSubmitInputDiv label {
  display: inline-block;
  padding: 8px 16px;
  background-color: #2196f3;
  color: #fff;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

#fileSubmitInputDiv input[type="file"] {
  display: none;
}

#fileSubmitInputDiv label:hover {
  background-color: #23527c;
}
</style>