import { Button, Dropdown, DropdownItem, DropdownMenu, DropdownTrigger } from '@nextui-org/react';
import React from 'react';
import { LuArrowDownAZ, LuArrowDownWideNarrow, LuArrowUpAZ, LuArrowUpWideNarrow } from 'react-icons/lu';

export default function SortBy({ orderBy, usergame }) {
  const [selectedKeys, setSelectedKeys] = React.useState(new Set(['nameDown']));

  return (
    <Dropdown>
      <DropdownTrigger>
        <Button variant="bordered">Ordenar por</Button>
      </DropdownTrigger>
      <DropdownMenu aria-label="Sort by button" onAction={(order) => orderBy(order)} disallowEmptySelection selectionMode="single" selectedKeys={selectedKeys} onSelectionChange={setSelectedKeys}>
        <DropdownItem key="nameDown" startContent={<LuArrowDownAZ />}>
          Nombre
        </DropdownItem>
        <DropdownItem key="nameUp" startContent={<LuArrowUpAZ />}>
          Nombre
        </DropdownItem>
        {usergame && (
          <>
            <DropdownItem key="hoursDown" startContent={<LuArrowDownWideNarrow />}>
              Horas jugadas
            </DropdownItem>
            <DropdownItem key="hoursUp" startContent={<LuArrowUpWideNarrow />}>
              Horas jugadas
            </DropdownItem>
          </>
        )}
      </DropdownMenu>
    </Dropdown>
  );
}
