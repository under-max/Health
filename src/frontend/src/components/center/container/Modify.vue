<template>  
    <div class="modify-black">
      <div class="modify-white">
        <h1>center 수정</h1>
        <!-- 센터검색창 -->
        <input v-model="centerName" />
        <button @click="centerSearchBtn">센터찾기</button>
        <select name="" id="" v-model="centerSelect">
          <option
            v-for="(centerData, index) in centerGetData"
            :value="centerData.id"
            :key="index"
          >
            {{ centerData.name }}
          </option>
        </select>        
        <hr/>
        <div v-if="dataSuccess && centerGetData !== null">          
          <div v-for="(centerData, index) in centerGetData" :key="index">
            <div v-if="centerData.id === centerSelect">
              <form enctype="multipart/form-data">
              <label for="centerId">ID</label>
              <input id="centerId" v-model="modifyData.id" /><br>              

              <label for="centerName">매장명 :</label>
              <input id="centerName" v-model="modifyData.name" /><br>
              
              <label for="centerAddress">주소 :</label>
              <input id="centerAddress" v-model="modifyData.address" /><br>
              
              <label for="centerInfo">정보 :</label>
              <input id="centerInfo" v-model="modifyData.info" /><br>
              
              <label for="centerPhoneNumber">전화번호 :</label>
              <input id="centerPhoneNumber" v-model="modifyData.phoneNumber" /><br> 

              <div v-for="(imgData, index) in centerData.fileName" :key="index">
                <div v-if="imgData != null">
                  <h1>사진</h1>
                  <img
                    :src="`https://bucket0503-1000011011001.s3.ap-northeast-2.amazonaws.com/health/${centerData.id}/${imgData}`"
                  />
                  <input type="checkbox" :value="imgData" v-model="checkBoxImg"/>
                </div>
              </div>
              <hr />
              <label for="addFile">+</label>
              <input id="addFile" type="file" multiple accept="image/*" @change="centerImage"/><br>
              <button @click="modifyCenter">수정하기</button>
              <button @click="deleteCenter">센터정보 삭제</button>
            </form>
            </div>
          </div>
        </div>

        <button @click="closeModal">나가기</button>       
        
    </div>
    </div>


</template>

<script setup>
import { ref, defineProps, defineEmits, watch } from 'vue';
import axios from 'axios';


//url
const url = "http://localhost:8090/";


//props 처리
const props = defineProps({
    centerModifyModal: Boolean,
    modelValue: String,
});

const emit = defineEmits(["closeCenterModifyModal", "modelValue"]);

const closeModal = () => {
    emit("closeCenterModifyModal");
}

const changeMessage = (message) => {
    emit("update:modelValue", message)
}

//수정을 위한 Data 수집
const modifyData = ref({
  id: "",
  name: "",
  address: "",
  phoneNumber: "",
  info: "",
  fileName: [],
})

// 아이디 확인해서 가져온 값으로 초기화
const centerSelect = ref(0);

watch(centerSelect, (newValue, oldValue)=>{
    centerGetData.value.map(data=>{              
        if(data.id === newValue){
            modifyData.value.id = data.id;
            modifyData.value.name = data.name;
            modifyData.value.address = data.address;
            modifyData.value.phoneNumber = data.phoneNumber;
            modifyData.value.info = data.info;         
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
    changeMessage(centerLength.value + "확인중");
    
    // modifyData.value.id = centerGetData.value.id;
  } catch (error) {
    console.log(error);
    dataSuccess.value = false;
  }
};


//수정 구현 로직

// checkBox 이미지 입력
const checkBoxImg = ref([]);

watch(checkBoxImg, (currentData, oldData)=>{    
    modifyData.value.fileName = currentData;
})

//업로드한 파일 처리 
const imgData = ref([]);
const centerImage = (e) => {
    const files = e.target.files;    
    for (let i = 0; i < files.length; i++){
        imgData.value.push(files[i]);
    }
}

const modifyCenter = async(e)=>{
    e.preventDefault();
    
    const centerModify = new FormData();
    // img data 추가
    
    // if(imgData.value.length !== 0){
      for (let i = 0; i < imgData.value.length; i++){
          centerModify.append("addImg", imgData.value[i]);
          console.log(imgData.value[i]);
      }
    // } else{
    //   centerModify.append("addImg", null);
    //   console.log(centerModify.get("addImg"));
    // }

    centerModify.append("id", modifyData.value.id);
    centerModify.append("name", modifyData.value.name);
    centerModify.append("address", modifyData.value.address);
    centerModify.append("phoneNumber", modifyData.value.phoneNumber);
    centerModify.append("info", modifyData.value.info);
    
    
    // if(modifyData.value.fileName.length !== 0){
      for(let i = 0; i < modifyData.value.fileName.length; i++){
          centerModify.append("removeImg", modifyData.value.fileName[i]);
          console.log(modifyData.value.fileName[i]);
      }
    // }else {
    //   centerModify.append("removeImg", []);
    //   console.log(centerModify.get("removeImg"));
    // }
        
    try{
        await axios.put(url+"center/modify",centerModify,{
            headers:{
                "Content-Type": "multipart/form-data",
            }
        });        
        centerModify.delete("removeImg");
        centerModify.delete("addImg");
    }catch(error){
        console.log(error);
    }
}



//삭제 구현 로직
const deleteCenter = async(e) => {   
    e.preventDefault();
    
    console.log(modifyData.value);
    try{
        await axios.delete(url+"center/delete", {data:modifyData.value})
        console.log("성공!");
    }catch(error){
        console.log(error);
    }
}


</script>

<style scoped>
/* //현재 전역으로 지정되어있어서 h1 글자색 바꿈  */
h1{
    color: black;
}
.modify-black {
  width: 90%;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.5);
  position: fixed;
  padding: 20px;

  overflow: hidden;
}
.modify-white {
  width: 100%;
  height: auto;
  background: white;
  border-radius: 30px;
  padding: 20px;

  max-height: 90vh;
  overflow-y: auto;
}


</style>