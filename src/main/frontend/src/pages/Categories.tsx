import React, { useEffect, useState } from 'react';
import { Tree } from 'antd';
import axios from 'axios';

interface Category {
  id: number;
  name: string;
  parent: Category | null;
}

interface TreeNode {
  title: string;
  key: string;
  children?: TreeNode[];
}

const Categories: React.FC = () => {
  const [categories, setCategories] = useState<Category[]>([]);
  const [treeData, setTreeData] = useState<TreeNode[]>([]);

  useEffect(() => {
    fetchCategories();
  }, []);

  const fetchCategories = async () => {
    try {
      const response = await axios.get<Category[]>('/api/categories');
      setCategories(response.data);
      const tree = buildTree(response.data);
      setTreeData(tree);
    } catch (error) {
      console.error('Failed to fetch categories', error);
    }
  };

  const buildTree = (flatList: Category[]): TreeNode[] => {
    const idMap = new Map<number, TreeNode>();
    const rootNodes: TreeNode[] = [];

    flatList.forEach((cat) => {
      idMap.set(cat.id, { title: cat.name, key: String(cat.id), children: [] });
    });

    flatList.forEach((cat) => {
      const node = idMap.get(cat.id)!;
      if (cat.parent?.id && idMap.has(cat.parent.id)) {
        idMap.get(cat.parent.id)!.children!.push(node);
      } else {
        rootNodes.push(node);
      }
    });

    return rootNodes;
  };

  return (
    <div>
      <h2>Categories</h2>
      <Tree treeData={treeData} defaultExpandAll />
    </div>
  );
};

export default Categories;