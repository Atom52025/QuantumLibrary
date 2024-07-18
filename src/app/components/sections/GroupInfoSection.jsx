'use client';

import { Avatar, AvatarGroup, Button, Checkbox, CheckboxGroup, Divider } from '@nextui-org/react';
import { useSession } from 'next-auth/react';
import React, { useEffect, useState } from 'react';
import { FaAngleLeft, FaAngleRight, FaRandom } from 'react-icons/fa';

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
  const pendingUsers = group.userGroups.filter((userGroup) => userGroup.accepted === false).map((userGroup) => userGroup.user);
  const acceptedUsers = group.userGroups.filter((userGroup) => userGroup.accepted === true).map((userGroup) => userGroup.user);

  return (
    <div className="h-100% min-w-[220px] bg-gray-800/50 shadow-inner flex flex-col ">
      <div className="h-[92%] w-full">
        <p className="uppercase text-center items-center p-3 text-2xl mt-5">{group.name}</p>
        <Divider className="my-4" />
        <p className="uppercase text-center text-xl text-white/50 mb-2">Usuarios</p>
        {acceptedUsers.map((user) => (
          <div key={user.id} className="flex flex-row items-center justify-left gap-3 px-5 py-3">
            <Avatar src={user.image} size="large" />
            <p className="text-center">{user.username}</p>
          </div>
        ))}
        <Divider className="my-4" />
        {pendingUsers.isEmpty && <p className="uppercase text-center text-xl text-white/50 mb-2">Pendientes</p>}
        {pendingUsers.map((user) => (
          <div key={user.id} className="flex flex-row items-center justify-left gap-3 px-5 py-3">
            <Avatar src={user.image} size="large" />
            <p className="text-center">{user.username}</p>
          </div>
        ))}
      </div>
      <GroupInviteModal groupId={group.id} />
      <GroupExitModal groupId={group.id} />
    </div>
  );
}
