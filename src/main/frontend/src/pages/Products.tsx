import React, { useEffect, useState } from 'react';
import { Table, Button, Modal, Form, Input, InputNumber, Switch, Select, Popconfirm, message } from 'antd';
import axios from 'axios';

const { Option } = Select;

interface ProductDTO {
  id?: number;
  name: string;
  description: string;
  price: number;
  available: boolean;
  categoryPath?: string;
  categoryId?: number;
}

export interface CategoryDTO {
  id: number;
  name: string;
  parent?: CategoryDTO | null;
}

const Products: React.FC = () => {
  const [products, setProducts] = useState<ProductDTO[]>([]);
  const [loading, setLoading] = useState(false);
  const [modalVisible, setModalVisible] = useState(false);
  const [editingProduct, setEditingProduct] = useState<ProductDTO | null>(null);
  const [form] = Form.useForm();
  const [categories, setCategories] = useState<CategoryDTO[]>([]);
  const [searchName, setSearchName] = useState('');

  useEffect(() => {
    axios.get<CategoryDTO[]>('/api/categories').then((res) => {
      setCategories(res.data);
    });
  }, []);

  useEffect(() => {
    fetchProducts();
  }, []);

  const fetchProducts = async () => {
    setLoading(true);
    try {
      const response = await axios.get<ProductDTO[]>('/api/products');
      setProducts(response.data);
      console.log(response.data);
    } catch (error) {
      console.error('Failed to fetch products', error);
    } finally {
      setLoading(false);
    }
  };

const handleFormSubmit = async (values: ProductDTO) => {
  try {
    const payload = {
      ...values,
      category: { id: values.categoryId },
    };
    delete (payload as any).categoryId;

    if (editingProduct?.id) {
      payload.id = editingProduct.id;
      await axios.put(`/api/products/${editingProduct.id}`, payload);
    } else {
      await axios.post('/api/products', payload);
    }

    message.success('Saved!');
    setModalVisible(false);
    fetchProducts();
  } catch (err) {
    message.error('Failed to save product');
  }
};

const handleDelete = async (id: number) => {
  try {
    await axios.delete(`/api/products/${id}`);
    message.success('Product deleted');
    fetchProducts();
  } catch {
    message.error('Failed to delete');
  }
};

  const columns = [
    {
      title: 'Name',
      dataIndex: 'name',
      key: 'name',
      sorter: (a: ProductDTO, b: ProductDTO) => a.name.localeCompare(b.name),
    },
    {
      title: 'Description',
      dataIndex: 'description',
      key: 'description',
    },
    {
      title: 'Price',
      dataIndex: 'price',
      key: 'price',
      sorter: (a: ProductDTO, b: ProductDTO) => a.price - b.price,
    },
    {
      title: 'Available',
      dataIndex: 'available',
      key: 'available',
      render: (available: boolean) => (available ? 'Yes' : 'No'),
      sorter: (a: ProductDTO, b: ProductDTO) => Number(b.available) - Number(a.available),
    },
    {
      title: 'Category',
      dataIndex: 'categoryPath',
      key: 'categoryPath',
      sorter: (a: ProductDTO, b: ProductDTO) =>
        (a.categoryPath ?? '').localeCompare(b.categoryPath ?? ''),
    },
  {
    title: 'Actions',
    key: 'actions',
    render: (_: any, record: ProductDTO) => (
      <>
        <Button
          size="small"
          onClick={() => {
            const productWithCategoryId = {
              ...record,
              categoryId: (record as any).categoryId,
            };

            setEditingProduct(productWithCategoryId);
            form.setFieldsValue(productWithCategoryId);
            setModalVisible(true);
          }}
          style={{ marginRight: 8 }}
        >
          Edit
        </Button>
        <Popconfirm
          title="Confirm delete?"
          onConfirm={() => handleDelete(record.id!)}
        >
          <Button size="small" danger>Delete</Button>
        </Popconfirm>
      </>
    ),
  }
  ];

const handleSearch = async () => {
  setLoading(true);
  try {
    const response = await axios.get<ProductDTO[]>('/api/products', {
      params: { name: searchName },
    });
    setProducts(response.data);
  } catch (err) {
    message.error('Failed to search');
  } finally {
    setLoading(false);
  }
};

  return (
    <>
    <div>
      <h2>Products</h2>
      <div style={{ marginBottom: 16, display: 'flex', alignItems: 'center' }}>
        <div style={{ display: 'flex', gap: 8 }}>
          <Input
            placeholder="Search by name"
            value={searchName}
            onChange={(e) => setSearchName(e.target.value)}
            style={{ width: 200 }}
            allowClear
          />
          <Button type="primary" onClick={handleSearch}>
            Search
          </Button>
          <Button onClick={() => { setSearchName(''); fetchProducts(); }}>
            Reset
          </Button>
        </div>

        <Button
          type="primary"
          style={{ marginLeft: 'auto' }}
          onClick={() => {
            setEditingProduct(null);
            setModalVisible(true);
          }}
        >
          Add Product
        </Button>
      </div>
      {Array.isArray(products) && products.length > 0 ? (
        <Table
          rowKey="id"
          columns={columns}
          dataSource={products}
          loading={loading}
          pagination={{ pageSize: 10 }}
        />
      ) : loading ? (
        <p>Loading...</p>
      ) : (
        <p>No products found.</p>
      )}
    </div>
    <Modal
      open={modalVisible}
      title={editingProduct ? 'Edit Product' : 'Add Product'}
      onCancel={() => setModalVisible(false)}
      onOk={() => form.submit()}
    >
      <Form form={form} layout="vertical" onFinish={handleFormSubmit}>
        <Form.Item name="name" label="Name" rules={[{ required: true }]}>
          <Input />
        </Form.Item>
        <Form.Item name="description" label="Description">
          <Input />
        </Form.Item>
        <Form.Item name="price" label="Price" rules={[{ required: true }]}>
          <InputNumber min={0} style={{ width: '100%' }} />
        </Form.Item>
        <Form.Item name="available" label="Available" valuePropName="checked">
          <Switch />
        </Form.Item>
        <Form.Item name="categoryId" label="Category" rules={[{ required: true }]}>
          <Select>
            {categories.map(cat => (
              <Option key={cat.id} value={cat.id}>{cat.name}</Option>
            ))}
          </Select>
        </Form.Item>
      </Form>
    </Modal>
    </>
  );
};

export default Products;