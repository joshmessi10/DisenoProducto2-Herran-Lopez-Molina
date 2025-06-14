<template>
  <div class="dashboard-container">
    <h2 class="dashboard-title">Welcome, {{ user.name }}!</h2>
    <div class="user-card">
      <div class="user-info">
        <h3>User Details</h3>
        <p><strong>Name:</strong> {{ user.name }}</p>
        <p><strong>Last Name:</strong> {{ user.lastName }}</p>
        <p><strong>Email:</strong> {{ user.email }}</p>
        <p><strong>Phone:</strong> {{ user.phone }}</p>
        <p>
          <strong>Account Created:</strong> {{ formatDate(user.creationDate) }}
        </p>
        <p><strong>Last Access:</strong> {{ formatDate(user.lastAccess) }}</p>
      </div>
      <div class="user-role">
        <h3>Role: {{ user.role }}</h3>
      </div>
    </div>
  </div>
</template>

<script>
import axiosInstance from "@/axios.js";
import router from "../router";

export default {
  name: "DashboardView",
  data() {
    return {
      user: {
        name: "Loading...",
        lastName: "",
        email: "",
        phone: "",
        creationDate: "",
        lastAccess: "",
        role: "",
      },
    };
  },
  methods: {
    getUserData: function () {
      let self = this;
      axiosInstance
        .get("/user-data")
        .then((response) => {
          console.log(response);
          self.$set(this, "user", response.data.data); // Assuming response data contains a "data" object with user info
        })
        .catch((errors) => {
          console.log(errors);
          router.push("/register");
        });
    },
    formatDate(date) {
      // Format the date to a more readable format (e.g., "Jan 1, 2024")
      const options = { year: "numeric", month: "long", day: "numeric" };
      return new Date(date).toLocaleDateString("en-US", options);
    },
  },
  mounted() {
    this.getUserData();
  },
};
</script>

<style scoped>
.dashboard-container {
  max-width: 900px;
  margin: 20px auto;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.dashboard-title {
  text-align: center;
  color: #333;
  font-size: 2em;
  margin-bottom: 20px;
}

.user-card {
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
  padding: 20px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

.user-info {
  flex: 1;
  padding-right: 20px;
}

.user-role {
  flex: 0 0 200px;
  padding-left: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #3a8dff;
  color: white;
  border-radius: 8px;
  height: 100%;
}

h3 {
  margin-bottom: 10px;
}

p {
  margin-bottom: 8px;
}

strong {
  font-weight: 600;
}
</style>
