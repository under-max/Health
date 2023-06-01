<template>
  <h4>이용권안내!</h4>
  <div class="container">
    <div class="row">
      <div class="col-md-4">
        <div>
          <Insert/>
        </div>
      </div>
      <div class="col-md-6">
        <div class="row g-10">
          <div class="col-md-8 col-lg-6 order-md-last">
            <h4 class="d-flex justify-content-between align-items-center mb-3">
              <span class="text-primary">Your cart</span>
              <span class="badge bg-primary rounded-pill">3</span>
            </h4>
            <ul class="list-group mb-3">
              <li class="list-group-item d-flex justify-content-between lh-sm">
                <div>
                  <h6 class="my-0">Product name</h6>
                  <small class="text-body-secondary">Brief description</small>
                </div>
                <span class="text-body-secondary">$12</span>
              </li>
              <li class="list-group-item d-flex justify-content-between lh-sm">
                <div>
                  <h6 class="my-0">Second product</h6>
                  <small class="text-body-secondary">Brief description</small>
                </div>
                <span class="text-body-secondary">$8</span>
              </li>
              <li class="list-group-item d-flex justify-content-between lh-sm">
                <div>
                  <h6 class="my-0">Third item</h6>
                  <small class="text-body-secondary">Brief description</small>
                </div>
                <span class="text-body-secondary">$5</span>
              </li>
              <li class="list-group-item d-flex justify-content-between bg-body-tertiary">
                <div class="text-success">
                  <h6 class="my-0">Promo code</h6>
                  <small>EXAMPLECODE</small>
                </div>
                <span class="text-success">−$5</span>
              </li>
              <li class="list-group-item d-flex justify-content-between">
                <span>Total (USD)</span>
                <strong>$20</strong>
              </li>
            </ul>

            <form class="card p-2">
              <div class="input-group">
                <input type="text" class="form-control" placeholder="Promo code">
                <button type="submit" class="btn btn-secondary">Redeem</button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  </div>


</template>
<script setup>
import Insert from "@/views/payment/Insert.vue";

</script>

<style scoped>
h4 {
  padding-top: 3rem;
  text-align: center;
}

.row {
  margin-right: -0.5rem;
  margin-left: -0.5rem;
}

.col-md-6 {
  padding-right: 0.5rem;
  padding-left: 0.5rem;
}
</style>