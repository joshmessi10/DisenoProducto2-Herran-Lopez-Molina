import Vue from "vue";
import VueRouter from "vue-router";
import HomeView from "../views/HomeView.vue";
import AboutView from "../views/AboutView.vue";
import AlertasView from "../views/AlertasView.vue";
import HistorialView from "../views/HistorialView.vue";
import LoginView from "../views/LoginView.vue";
import RegisterView from "../views/RegisterView.vue";
import Dashboard from "../components/DashboardView.vue";
import RegisterWallet from "../views/RegisterWallet.vue";
import RegisterCellphone from "../views/RegisterCellphone.vue";

Vue.use(VueRouter);

const routes = [
  {
    path: "/home",
    name: "home",
    component: HomeView,
  },
  {
    path: "/about",
    name: "about",
    component: AboutView,
  },
  {
    path: "/alertas",
    name: "alertas",
    component: AlertasView,
  },
  {
    path: "/historial",
    name: "historial",
    component: HistorialView,
  },
  { path: "/", name: "login", component: LoginView },
  { path: "/register", name: "register", component: RegisterView },
  {
    path: "/dashboard",
    name: "Dashboard",
    component: Dashboard,
  },
  {
    path: "/register-wallet",
    name: "RegisterWallet",
    component: RegisterWallet,
  },
  {
    path: "/register-cellphone",
    name: "RegisterCellphone",
    component: RegisterCellphone,
  },
];

const router = new VueRouter({
  mode: "history", // Para usar rutas amigables sin hash
  routes,
});

export default router;
