import { Layout, Menu } from 'antd';
import {
  AppstoreOutlined,
  BarsOutlined,
  LogoutOutlined,
} from '@ant-design/icons';
import { Link, Outlet, useLocation, useNavigate } from 'react-router-dom';
import { useAuth } from '../auth/AuthContext';

const { Header, Content, Sider } = Layout;

const MainLayout: React.FC = () => {
  const location = useLocation();
  const { logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = async () => {
    await logout();
    navigate('/login');
  };

  return (
    <Layout style={{ minHeight: '100vh' }}>
      <Sider breakpoint="lg" collapsedWidth="0">
        <div
          style={{
            height: '100%',
            display: 'flex',
            flexDirection: 'column',
          }}
        >
          <div style={{ height: 32, margin: 16, color: '#fff', fontWeight: 'bold' }}>
            QIMA App
          </div>

          <Menu
            theme="dark"
            mode="inline"
            selectedKeys={[location.pathname]}
            style={{ flexGrow: 1 }}
          >
            <Menu.Item key="/products" icon={<AppstoreOutlined />}>
              <Link to="/products">Products</Link>
            </Menu.Item>
            <Menu.Item key="/categories" icon={<BarsOutlined />}>
              <Link to="/categories">Categories</Link>
            </Menu.Item>
          </Menu>

          <Menu theme="dark" mode="inline">
            <Menu.Item
              key="logout"
              icon={<LogoutOutlined />}
              onClick={handleLogout}
            >
              Logout
            </Menu.Item>
          </Menu>
        </div>
      </Sider>
      <Layout>
        <Header style={{ background: '#fff', padding: 0 }} />
        <Content style={{ margin: '24px 16px 0' }}>
          <div style={{ padding: 24, background: '#fff', minHeight: 360 }}>
            <Outlet />
          </div>
        </Content>
      </Layout>
    </Layout>
  );
};

export default MainLayout;