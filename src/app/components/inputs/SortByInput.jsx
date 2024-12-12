import { Button, Dropdown, DropdownItem, DropdownMenu, DropdownTrigger } from '@nextui-org/react';
import React, { useState } from 'react';
import { LuArrowDownAZ, LuArrowDownWideNarrow, LuArrowUpAZ, LuArrowUpWideNarrow } from 'react-icons/lu';

export default function SortByInput({ setOrder, usergame = false, groupgame = false }) {
  const [selectedKeys, setSelectedKeys] = useState(new Set(['nameDown']));

  // Define menu items based on `usergame` dynamically
  const menuItems = [
    { key: 'nameDown', label: 'Nombre (A-Z)', icon: <LuArrowDownAZ /> },
    { key: 'nameUp', label: 'Nombre (Z-A)', icon: <LuArrowUpAZ /> },
    ...(usergame
      ? [
          { key: 'hoursDown', label: 'Horas jugadas (Mayor a menor)', icon: <LuArrowDownWideNarrow /> },
          { key: 'hoursUp', label: 'Horas jugadas (Menor a mayor)', icon: <LuArrowUpWideNarrow /> },
        ]
      : []),
    ...(groupgame
      ? [
          { key: 'mostVoted', label: 'Más votado', icon: <LuArrowDownWideNarrow /> },
          { key: 'leastVoted', label: 'Menos votado', icon: <LuArrowUpWideNarrow /> },
        ]
      : []),
  ];

  return (
    <Dropdown>
      <DropdownTrigger>
        <Button variant="bordered" className="lg:flex-grow-0 flex-grow ">
          Ordenar por
        </Button>
      </DropdownTrigger>
      <DropdownMenu aria-label="Sort by button" onAction={(order) => setOrder(order)} disallowEmptySelection selectionMode="single" selectedKeys={selectedKeys} onSelectionChange={setSelectedKeys}>
        {menuItems.map((item) => (
          <DropdownItem key={item.key} startContent={item.icon}>
            {item.label}
          </DropdownItem>
        ))}
      </DropdownMenu>
    </Dropdown>
  );
}
