import React, { useEffect, useState } from 'react';
import { Table } from 'antd';
import axios from 'axios';

interface ProductDTO {
  id: number;
  name: string;
  description: string;
  price: number;
  available: boolean;
  categoryPath: string;
}

const Products: React.FC = () => {
  const [products, setProducts] = useState<ProductDTO[]>([]);
  const [loading, setLoading] = useState(false);

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
      sorter: (a: ProductDTO, b: ProductDTO) => a.categoryPath.localeCompare(b.categoryPath),
    },
  ];

  return (
    <div>
      <h2>Products</h2>
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
  );
};

export default Products;