package UTCN_IMDB.demo.service;

import UTCN_IMDB.demo.model.Role;
import UTCN_IMDB.demo.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor

public class RoleService {
    private final RoleRepository roleRepository;

    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    public Role getRoleByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName).orElseThrow(
                () -> new IllegalStateException("Role with roleName " + roleName + " not found"));
    }

    public Role getRoleById(UUID uuid) {
        return roleRepository.findById(uuid).orElseThrow(
                () -> new IllegalStateException("Role with id " + uuid + " not found"));
    }

    public Role addRole(Role role) {
        return roleRepository.save(role);
    }

    public Role updateRole(UUID uuid, Role role) {
        Role existingRole =
                roleRepository.findById(uuid).orElseThrow(
                        () -> new IllegalStateException("Role with uuid " + uuid + " not found"));
        existingRole.setRoleName(role.getRoleName());
        existingRole.setRoleId(role.getRoleId());

        return roleRepository.save(existingRole);
    }

    public void deleteRole(UUID uuid) {
        roleRepository.deleteById(uuid);
    }

}
