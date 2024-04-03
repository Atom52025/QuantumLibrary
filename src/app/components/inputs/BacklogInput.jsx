import { Select, SelectItem } from '@nextui-org/react';

export default function BacklogInput({ backlog, setBacklog, totalAchievements, achievements }) {
  return (
    <Select
      label="Selecciona una backlog"
      className="w-full"
      selectedKeys={backlog}
      onSelectionChange={setBacklog}
      isDisabled={totalAchievements === achievements && totalAchievements > 0}
      description={totalAchievements === achievements && totalAchievements > 0 ? 'Juego completado' : 'Prioridad con la que te gustaria completar este juego (1 > 3)'}>
      <SelectItem key={1} value={1}>
        Backlog 1
      </SelectItem>
      <SelectItem key={2} value={2}>
        Backlog 2
      </SelectItem>
      <SelectItem key={3} value={3}>
        Backlog 3
      </SelectItem>
    </Select>
  );
}
