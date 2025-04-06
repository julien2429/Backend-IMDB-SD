package UTCN_IMDB.demo.service;

import UTCN_IMDB.demo.model.Role;
import UTCN_IMDB.demo.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleServiceTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetRoles() {
        // given
        List<Role> roles = List.of(new Role(), new Role());
        when(roleRepository.findAll()).thenReturn(roles);

        // when
        List<Role> result = roleService.getRoles();

        // then
        assertEquals(2, result.size());
        assertEquals(roles, result);
        verify(roleRepository, times(1)).findAll();
    }

    @Test
    void testGetRoleByRoleName() {
        // given
        String roleName = "ADMIN";
        Role role = new Role();
        role.setRoleName(roleName);
        when(roleRepository.findByRoleName(roleName)).thenReturn(Optional.of(role));

        // when
        Role result = roleService.getRoleByRoleName(roleName);

        // then
        assertEquals(roleName, result.getRoleName());
        verify(roleRepository, times(1)).findByRoleName(roleName);
    }

    @Test
    void testGetRoleByRoleNameNotFound() {
        // given
        String roleName = "ADMIN";
        when(roleRepository.findByRoleName(roleName)).thenReturn(Optional.empty());

        // when, then
        assertThrows(IllegalStateException.class, () -> roleService.getRoleByRoleName(roleName));
        verify(roleRepository, times(1)).findByRoleName(roleName);
    }

    @Test
    void testGetRoleById() {
        // given
        UUID uuid = UUID.randomUUID();
        Role role = new Role();
        role.setRoleId(uuid);
        when(roleRepository.findById(uuid)).thenReturn(Optional.of(role));

        // when
        Role result = roleService.getRoleById(uuid);

        // then
        assertEquals(uuid, result.getRoleId());
        verify(roleRepository, times(1)).findById(uuid);
    }

    @Test
    void testGetRoleByIdNotFound() {
        // given
        UUID uuid = UUID.randomUUID();
        when(roleRepository.findById(uuid)).thenReturn(Optional.empty());

        // when, then
        assertThrows(IllegalStateException.class, () -> roleService.getRoleById(uuid));
        verify(roleRepository, times(1)).findById(uuid);
    }

    @Test
    void testAddRole() {
        // given
        Role roleToSave = new Role();
        roleToSave.setRoleName("USER");
        Role savedRole = new Role();
        savedRole.setRoleId(UUID.randomUUID());
        savedRole.setRoleName(roleToSave.getRoleName());

        when(roleRepository.save(roleToSave)).thenReturn(savedRole);

        // when
        Role result = roleService.addRole(roleToSave);

        // then
        assertNotNull(result.getRoleId());
        assertEquals(roleToSave.getRoleName(), result.getRoleName());
        verify(roleRepository, times(1)).save(roleToSave);
    }

    @Test
    void testUpdateRole() {
        // given
        UUID uuid = UUID.randomUUID();
        Role existingRole = new Role();
        existingRole.setRoleId(uuid);
        existingRole.setRoleName("USER");

        Role roleUpdate = new Role();
        roleUpdate.setRoleName("ADMIN");

        Role updatedRole = new Role();
        updatedRole.setRoleId(uuid);
        updatedRole.setRoleName(roleUpdate.getRoleName());

        when(roleRepository.findById(uuid)).thenReturn(Optional.of(existingRole));
        when(roleRepository.save(any(Role.class))).thenReturn(updatedRole);

        // when
        Role result = roleService.updateRole(uuid, roleUpdate);

        // then
        assertEquals(roleUpdate.getRoleName(), result.getRoleName());
        verify(roleRepository, times(1)).findById(uuid);
        verify(roleRepository, times(1)).save(any(Role.class));
    }

    @Test
    void testUpdateRoleNotFound() {
        // given
        UUID uuid = UUID.randomUUID();
        Role roleUpdate = new Role();

        when(roleRepository.findById(uuid)).thenReturn(Optional.empty());

        // when, then
        assertThrows(IllegalStateException.class, () -> roleService.updateRole(uuid, roleUpdate));
        verify(roleRepository, times(1)).findById(uuid);
        verify(roleRepository, times(0)).save(any(Role.class));
    }

    @Test
    void testDeleteRole() {
        // given
        UUID uuid = UUID.randomUUID();
        doNothing().when(roleRepository).deleteById(uuid);

        // when
        roleService.deleteRole(uuid);

        // then
        verify(roleRepository, times(1)).deleteById(uuid);
    }
}