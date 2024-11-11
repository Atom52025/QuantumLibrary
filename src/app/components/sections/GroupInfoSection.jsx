'use client';

import { Avatar, AvatarGroup, Button, Checkbox, CheckboxGroup, Divider, ScrollShadow } from '@nextui-org/react';
import { useSession } from 'next-auth/react';
import React, { useEffect, useState } from 'react';
import { FaAngleLeft, FaAngleRight, FaRandom } from 'react-icons/fa';
import { IoIosArrowDropdown, IoIosArrowDropup } from 'react-icons/io';

import Link from 'next/link';

import { DELETE, PATCH } from '@/app/api/tokenRequest';
import FilterBar from '@/app/components/inputs/FilterBar';
import SortByInput from '@/app/components/inputs/SortByInput';
import AddUserGameModal from '@/app/components/modals/AddUserGameModal';
import GroupCreateModal from '@/app/components/modals/GroupCreateModal';
import GroupExitModal from '@/app/components/modals/GroupExitModal';
import GroupInviteModal from '@/app/components/modals/GroupInviteModal';
import SteamImportModal from '@/app/components/modals/SteamImportModal';
import UserGameCard from '@/app/components/user/UserGameCard';

export default function GroupsSection({ group }) {
  const [isOpen, setIsOpen] = useState(false);
  const [isLgScreen, setIsLgScreen] = useState(false);

  const pendingUsers = group.userGroups.filter((userGroup) => userGroup.accepted === false).map((userGroup) => userGroup.user);
  const acceptedUsers = group.userGroups.filter((userGroup) => userGroup.accepted === true).map((userGroup) => userGroup.user);

  // Checks if window is lg
  useEffect(() => {
    // Check screen width on mount
    const handleResize = () => {
      setIsLgScreen(window.innerWidth >= 768);
    };
    handleResize();
    window.addEventListener('resize', handleResize);
    return () => window.removeEventListener('resize', handleResize);
  }, []);

  return (
    <div className={`md:min-h-full md:min-w-[220px] md:w-auto w-full bg-gray-800/50 shadow-inner flex flex-col md:p-0 p-3 gap-5 relative´ ${isOpen || isLgScreen ? 'max-h-[100%]' : 'max-h-12'}`}>
      <button onClick={() => setIsOpen(!isOpen)} className="uppercase text-center text-lg justify-center flex flex-row gap-3 items-center md:hidden">
        Grupo
        {isOpen ? <IoIosArrowDropup /> : <IoIosArrowDropdown />}
      </button>

      <div className="overflow-hidden h-full flex flex-col">
        <ScrollShadow hideScrollBar className="flex-grow w-full overflow-auto">
          <p className="uppercase text-center items-center p-2 text-2xl lg:mt-5">{group.name}</p>
          <Divider className="my-1" />
          <p className="uppercase text-center text-xl text-white/50 p-2">Usuarios</p>
          {acceptedUsers.map((user) => (
            <div key={user.id} className="flex flex-row items-center justify-left gap-3 px-5 pb-2">
              <Avatar src={user.image} size="large" />
              <p className="text-center">{user.username}</p>
            </div>
          ))}
          <Divider className="my-1" />
          {!pendingUsers.isEmpty && <p className="uppercase text-center text-xl text-white/50 p-2">Pendientes</p>}
          {pendingUsers.map((user) => (
            <div key={user.id} className="flex flex-row items-center justify-left gap-3 px-5 pb-2">
              <Avatar src={user.image} size="large" />
              <p className="text-center">{user.username}</p>
            </div>
          ))}
        </ScrollShadow>
        <GroupInviteModal groupId={group.id} />
        <GroupExitModal groupId={group.id} />
      </div>
    </div>
  );
}
